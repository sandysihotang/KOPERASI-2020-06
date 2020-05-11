package io.github.sandy.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import io.github.sandy.model.Angsuran;
import io.github.sandy.model.Pinjaman;
import io.github.sandy.model.User;
import io.github.sandy.repository.AngsuranRepository;
import io.github.sandy.repository.PinjamanRepository;
import io.github.sandy.repository.UserRepository;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
public class Pdf {

    @Autowired
    PinjamanRepository pinjamanRepository;

    @Autowired
    AngsuranRepository angsuranRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/getdatapengajupdf/{id}")
    public void createPdf(
            @PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws IOException {

        VelocityEngine ve = new VelocityEngine();

        Principal principal = request.getUserPrincipal();
        User user = userRepository.findByUsername(principal.getName()).get();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("templates/pdf.vm");
        /* create a context and add data */
        VelocityContext context = new VelocityContext();
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");

        Pinjaman pinjaman = pinjamanRepository.getFirstById(id);
        context.put("noPinjaman", pinjaman.getKodePinjaman());
        context.put("namaKoperasi", pinjaman.getKoperasi().getNamaKoperasi());
        context.put("nama", String.format("%s %s", pinjaman.getUser().getUserDetail().getFirstName(), pinjaman.getUser().getUserDetail().getLastName()));
        context.put("danapinjaman", kursIndonesia.format(pinjaman.getJumlahPinjaman()));
        context.put("bulan", pinjaman.getTenor());
        context.put("persen", pinjaman.getPengaturanPinjaman().getBungaPinjaman());
        List<Angsuran> angsuran = angsuranRepository.getAllByPinjamanOrderByUrutanKeAsc(pinjaman);
        context.put("bulanAwal", simpleDateFormat.format(new Date()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, pinjaman.getPengaturanPinjaman().getAmbangBatasDenda());
        calendar.add(Calendar.MONTH, pinjaman.getTenor());
        context.put("bulanAkhir", simpleDateFormat.format(calendar.getTime()));
        context.put("pokok", kursIndonesia.format(pinjaman.getJumlahPinjaman() / pinjaman.getTenor()));
        context.put("totalPokok", kursIndonesia.format(pinjaman.getJumlahPinjaman()));
        context.put("jasaPinjaman", kursIndonesia.format(pinjaman.getJumlahPinjaman() * (pinjaman.getPengaturanPinjaman().getBungaPinjaman() / 100)));
        context.put("totalJasaPinjaman", kursIndonesia.format(pinjaman.getJumlahPinjaman() * (pinjaman.getPengaturanPinjaman().getBungaPinjaman() / 100) * pinjaman.getTenor()));
        context.put("total", kursIndonesia.format((pinjaman.getJumlahPinjaman() * (pinjaman.getPengaturanPinjaman().getBungaPinjaman() / 100)) + pinjaman.getJumlahPinjaman() / pinjaman.getTenor()));
        context.put("totalJumlah", kursIndonesia.format((pinjaman.getJumlahPinjaman() * (pinjaman.getPengaturanPinjaman().getBungaPinjaman() / 100) * pinjaman.getTenor()) + pinjaman.getJumlahPinjaman()));
        context.put("pengurus", String.format("%s %s", user.getUserDetail().getFirstName(), user.getUserDetail().getLastName()));
        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        /* show the World */

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos = generatePdf(writer.toString());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + "pengajuan".replace(" ", "_"));

        InputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    public ByteArrayOutputStream generatePdf(String html) {

        String pdfFilePath = "";
        PdfWriter pdfWriter = null;

        // create a new document
        Document document = new Document();
        try {

            document = new Document();
            // document header attributes
            document.addAuthor("Sandy");
            document.addAuthor("Sandy");
            document.addCreationDate();
            document.addProducer();
            document.addCreator("Sandy");
            document.addTitle("Good Luck");
            document.setPageSize(PageSize.LETTER);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            // open document
            document.open();

            XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
            xmlWorkerHelper.getDefaultCssResolver(true);
            xmlWorkerHelper.parseXHtml(pdfWriter, document, new StringReader(
                    html));
            // close the document
            document.close();

            return baos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
