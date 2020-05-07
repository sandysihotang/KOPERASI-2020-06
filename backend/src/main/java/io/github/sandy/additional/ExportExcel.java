package io.github.sandy.additional;

import io.github.sandy.model.Produk;
import io.github.sandy.model.ProdukBaru;
import io.github.sandy.model.Vendor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExportExcel {
    public String toIDR(Integer num) {
        String nums = String.format("%d", num);

        String ans = "";
        int coma = 0;
        for (int i = nums.length() - 1; i >= 0; i--) {
            ans = String.format("%s%c", ans, nums.charAt(i));
            if (coma == 2 && i != 0) {
                ans = String.format("%s,", ans);
                coma = 0;
            } else {
                coma++;
            }
        }
        String res = "Rp ";
        for (int i = ans.length() - 1; i >= 0; i--) {
            res = String.format("%s%c", res, ans.charAt(i));
        }
        return res;
    }

    public ByteArrayInputStream downloadBarangMasuk(Vendor vendor) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Vendor");

            Row rowNama = sheet.createRow(0);
            rowNama.createCell(0).setCellValue("Nama vendor");
            rowNama.createCell(1).setCellValue(vendor.getNamaVendor());
            Row rowAlamat = sheet.createRow(1);
            rowAlamat.createCell(0).setCellValue("Alamat vendor");
            rowAlamat.createCell(1).setCellValue(vendor.getAlamatVendor());
            Row roNoTelepon = sheet.createRow(2);
            roNoTelepon.createCell(0).setCellValue("No Telepon vendor");
            roNoTelepon.createCell(1).setCellValue(vendor.getNoTelepon());

            Row row = sheet.createRow(3);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell = row.createCell(0);
            cell.setCellValue("Nama Barang");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("Kode Barang");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("Jumlah Masuk");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("Harga Barang");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(4);
            cell.setCellValue("Harga Sub Total");
            cell.setCellStyle(headerCellStyle);

//             Creating data rows for each customer
            int i = 3;
            int totalHarga = 0;
            for (ProdukBaru produkBaru1 : vendor.getProdukBaru()) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(produkBaru1.getProduk().getNamaProduk());
                dataRow.createCell(1).setCellValue(produkBaru1.getProduk().getKodeProduk());
                dataRow.createCell(2).setCellValue(produkBaru1.getJumlahProduk());
                dataRow.createCell(3).setCellValue(toIDR(produkBaru1.getHargaBeli()));
                dataRow.createCell(4).setCellValue(toIDR(produkBaru1.getHargaBeli() * produkBaru1.getJumlahProduk()));
                totalHarga += (produkBaru1.getHargaBeli() * produkBaru1.getJumlahProduk());
                i++;
            }
            Row dataRow = sheet.createRow(i + 1);
            dataRow.createCell(3).setCellValue("Total Harga");
            dataRow.createCell(4).setCellValue(toIDR(totalHarga));


            // Making size of column auto resize to fit with data
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
