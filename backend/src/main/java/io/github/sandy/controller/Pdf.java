package io.github.sandy.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import io.github.sandy.gdrive.DriveQuickstart;
import io.github.sandy.repository.*;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class Pdf {

    @Autowired
    PinjamanRepository pinjamanRepository;

    @Autowired
    AngsuranRepository angsuranRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    KoperasiRepository koperasiRepository;

    @Autowired
    DetailUserRepository detailUserRepository;

    @Autowired
    PengaturanPinjamanRepository pengaturanPinjamanRepository;

    @GetMapping("/api/getdatapengajupdf/{id}")
    public void createPdf(
            @PathVariable("id") Integer id, HttpServletResponse response, HttpServletRequest request) throws IOException {

        VelocityEngine ve = new VelocityEngine();

        Principal principal = request.getUserPrincipal();
        String uname = principal.getName();
        Map<String, Object> user = userRepository.getUserUsername(uname);
        Map<String, Object> userDetailPengurus = detailUserRepository.findUserDetail((Integer) user.get("id"));
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

        Map<String, Object> pinjaman = pinjamanRepository.getFirstById(id);
        context.put("noPinjaman", pinjaman.get("kode_pinjaman"));
        Map<String, Object> koperasi = koperasiRepository.getKoperasiID((Integer) pinjaman.get("id_koperasi"));
        context.put("namaKoperasi", koperasi.get("nama_koperasi"));
        Map<String, Object> userDetail = detailUserRepository.findUserDetail((Integer) pinjaman.get("id_user"));
        context.put("nama", String.format("%s %s", userDetail.get("first_name"), userDetail.get("last_name")));
        context.put("danapinjaman", kursIndonesia.format(pinjaman.get("jumlah_pinjaman")));
        context.put("bulan", pinjaman.get("tenor"));
        Map<String, Object> pengaturanPinjaman = pengaturanPinjamanRepository.getFirstByPinjaman((Integer) pinjaman.get("id"));
        context.put("persen", pengaturanPinjaman.get("bunga_pinjaman"));
        context.put("bulanAwal", simpleDateFormat.format(new Date()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, (Integer) pengaturanPinjaman.get("ambang_batas_denda"));
        calendar.add(Calendar.MONTH, (Integer) pinjaman.get("tenor"));
        context.put("bulanAkhir", simpleDateFormat.format(calendar.getTime()));
        context.put("pokok", kursIndonesia.format((Double) pinjaman.get("jumlah_pinjaman") / (Integer) pinjaman.get("tenor")));
        context.put("totalPokok", kursIndonesia.format(pinjaman.get("jumlah_pinjaman")));
        context.put("jasaPinjaman", kursIndonesia.format((Double) pinjaman.get("jumlah_pinjaman") * ((Double) pengaturanPinjaman.get("bunga_pinjaman") / 100)));
        context.put("totalJasaPinjaman", kursIndonesia.format((Double) pinjaman.get("jumlah_pinjaman") * ((Double) pengaturanPinjaman.get("bunga_pinjaman") / 100) * (Integer) pinjaman.get("tenor")));
        context.put("total", kursIndonesia.format(((Double) pinjaman.get("jumlah_pinjaman") * ((Double) pengaturanPinjaman.get("bunga_pinjaman") / 100)) + (Double) pinjaman.get("jumlah_pinjaman") / (Integer) pinjaman.get("tenor")));
        context.put("totalJumlah", kursIndonesia.format(((Double) pinjaman.get("jumlah_pinjaman") *
                ((Double) pengaturanPinjaman.get("bunga_pinjaman") / 100) *
                (Integer) pinjaman.get("tenor")) + (Double) pinjaman.get("jumlah_pinjaman")));
        context.put("pengurus", String.format("%s %s", userDetailPengurus.get("first_name"), userDetailPengurus.get("last_name")));
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

    @RequestMapping(value = "/testwrew", method = RequestMethod.GET)
    public void call() throws Exception {
        DriveQuickstart driveQuickstart = new DriveQuickstart();
        driveQuickstart.call();
    }
}
