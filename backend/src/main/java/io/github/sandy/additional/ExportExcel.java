package io.github.sandy.additional;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.github.sandy.model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public ByteArrayInputStream downloadLabaDanMasuk(Map<String, Object> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Laba Dan Pemasukan");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));

            Row rowHeader = sheet.createRow(1);
            CellStyle headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell1 = rowHeader.createCell(1);
            cell1.setCellValue(String.valueOf(data.get("namaKoperasi")));
            cell1.setCellStyle(headerCellStyle1);

            rowHeader = sheet.createRow(2);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(1);
            cell1.setCellValue(String.valueOf(data.get("periode")));
            cell1.setCellStyle(headerCellStyle1);

            rowHeader = sheet.createRow(0);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(1);
            cell1.setCellValue("Laba Dan Pemasukan");
            cell1.setCellStyle(headerCellStyle1);

            Row row = sheet.createRow(4);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell = row.createCell(1);
            cell.setCellValue("Realisasi Jasa");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("realisasiJasa")).intValue()));

            row = sheet.createRow(5);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(1);
            cell.setCellValue("Tunggakan");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("tunggakan")).intValue()));

            row = sheet.createRow(6);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(1);
            cell.setCellValue("Simpanan Pokok");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("simpananPokok")).intValue()));


            row = sheet.createRow(7);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(1);
            cell.setCellValue("Simpanan Wajib");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("simpananWajib")).intValue()));

            row = sheet.createRow(8);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(1);
            cell.setCellValue("Simpanan Sukarela");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("simpananSukarela")).intValue()));

            row = sheet.createRow(10);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(1);
            cell.setCellValue("Total");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("simpananSukarela")).intValue()
                    + ((Long) data.get("simpananWajib")).intValue()
                    + ((Long) data.get("simpananPokok")).intValue()
                    + ((Long) data.get("tunggakan")).intValue()
                    + ((Long) data.get("realisasiJasa")).intValue()));


            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ByteArrayInputStream downloadTransaksiSimpanan(Map<String, Object> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transaksi Simpanan");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));

            Row rowHeader = sheet.createRow(1);
            CellStyle headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell1 = rowHeader.createCell(3);
            cell1.setCellValue(String.valueOf(data.get("namaKoperasi")));
            cell1.setCellStyle(headerCellStyle1);

            rowHeader = sheet.createRow(2);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(3);
            cell1.setCellValue(String.valueOf(data.get("periode")));
            cell1.setCellStyle(headerCellStyle1);

            rowHeader = sheet.createRow(0);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(3);
            cell1.setCellValue("Transaksi Simpanan");
            cell1.setCellStyle(headerCellStyle1);

            Row row = sheet.createRow(4);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell = row.createCell(1);
            cell.setCellValue("No Transaksi");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("Nama Nasabah");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("Tipe Transaksi");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(4);
            cell.setCellValue("Produk");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(5);
            cell.setCellValue("Tanggal Transaksi");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(6);
            cell.setCellValue("Nominal Transaksi");
            cell.setCellStyle(headerCellStyle);

            int i = 4;
            List<Map<String, Object>> aktivasiSimpananList = (List<Map<String, Object>>) data.get("dataTable");
            for (Map<String, Object> dataTable : aktivasiSimpananList) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(1).setCellValue(String.valueOf(dataTable.get("noTransaksi")));
                dataRow.createCell(2).setCellValue(String.valueOf(dataTable.get("nama")));
                dataRow.createCell(3).setCellValue(String.valueOf(dataTable.get("tipeTransaksi")));
                dataRow.createCell(4).setCellValue(String.valueOf(dataTable.get("produk")));
                String pattern = "MM/dd/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                dataRow.createCell(5).setCellValue(df.format((Date) dataTable.get("tglTransaksi")));
                dataRow.createCell(6).setCellValue(toIDR((Integer) dataTable.get("nominal")));
                i++;
            }


            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ByteArrayInputStream downloadAnggotaAktif(Map<String, Object> data) throws Exception {


        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Anggota Aktif Koperasi");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));

        Row rowHeader = sheet.createRow(1);
        CellStyle headerCellStyle1 = workbook.createCellStyle();
        headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // Creating header
        Cell cell1 = rowHeader.createCell(2);
        cell1.setCellValue(String.valueOf(data.get("namaKoperasi")));
        cell1.setCellStyle(headerCellStyle1);

        rowHeader = sheet.createRow(0);
        headerCellStyle1 = workbook.createCellStyle();
        headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // Creating header
        cell1 = rowHeader.createCell(2);
        cell1.setCellValue("Anggota Koperasi");
        cell1.setCellStyle(headerCellStyle1);
        rowHeader = sheet.createRow(1);
        headerCellStyle1 = workbook.createCellStyle();
        headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // Creating header
        cell1 = rowHeader.createCell(1);
        cell1.setCellValue("Anggota : " + data.get("totalAnggota"));
        cell1.setCellStyle(headerCellStyle1);

        Row row = sheet.createRow(4);
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


        List<String> cid = new ArrayList<>();
        JSONArray jsonArray = new JSONArray((String) data.get("pengaturanField"));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject rowTable = jsonArray.getJSONObject(i);
            Cell cell = row.createCell(i + 1);
            cell.setCellValue(rowTable.getString("label"));
            cell.setCellStyle(headerCellStyle);
            cid.add((String) rowTable.get("cid"));
        }


        Set<AnggotaKoperasi> temp = (Set<AnggotaKoperasi>) data.get("anggota");
        AnggotaKoperasi[] dataCol = new AnggotaKoperasi[temp.size()];
        temp.toArray(dataCol);
        for (int i = 0; i < dataCol.length; i++) {
            Row rowData = sheet.createRow(4 + i + 1);
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            JSONArray rowTable = new JSONArray(dataCol[i].getData());
            JSONObject temps = new JSONObject();
            for (int k = 0; k < rowTable.length(); k++) {
                JSONObject jsonObject = (JSONObject) rowTable.get(k);
                Object res = jsonObject.get("value");
                StringBuilder ans = new StringBuilder();
                if (res instanceof JSONObject) {
                    JSONObject tempo = (JSONObject) res;
                    Iterator<String> an = tempo.keys();
                    while (an.hasNext()) {
                        ans.append(" ").append(tempo.get(an.next()));
                    }
                } else {
                    ans.append((String) res);
                }
                temps.put(jsonObject.getString("uid"), ans.toString());
            }

            for (int j = 0; j < cid.size(); j++) {
                Cell cell = rowData.createCell(j + 1);
                cell.setCellValue(String.valueOf(temps.get(cid.get(j))));
            }
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public ByteArrayInputStream downloadPinjaman(Map<String, Object> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transaksi Simpanan");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));

            Row rowHeader = sheet.createRow(1);
            CellStyle headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell1 = rowHeader.createCell(3);
            cell1.setCellValue(String.valueOf(data.get("namaKoperasi")));
            cell1.setCellStyle(headerCellStyle1);

            rowHeader = sheet.createRow(2);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(3);
            cell1.setCellValue(String.valueOf(data.get("periode")));
            cell1.setCellStyle(headerCellStyle1);

            rowHeader = sheet.createRow(0);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(3);
            cell1.setCellValue("Pinjaman");
            cell1.setCellStyle(headerCellStyle1);

            rowHeader = sheet.createRow(5);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(3);
            cell1.setCellValue("Total Pinjaman");
            rowHeader.createCell(4).setCellValue(toIDR(new Double((double) data.get("totPinjaman")).intValue()));

            rowHeader = sheet.createRow(6);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(3);
            cell1.setCellValue("Total Laba");
            rowHeader.createCell(4).setCellValue(toIDR(new Double((double) data.get("totLaba")).intValue()));

            rowHeader = sheet.createRow(7);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(3);
            cell1.setCellValue("Total");
            rowHeader.createCell(4).setCellValue(toIDR(new Double((double) data.get("totLaba")).intValue() + new Double((double) data.get("totPinjaman")).intValue()));


            Row row = sheet.createRow(8);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell = row.createCell(1);
            cell.setCellValue("No Transaksi");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("Nama Nasabah");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("Jumlah Pinjaman");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(4);
            cell.setCellValue("Laba");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(5);
            cell.setCellValue("Tenor");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(6);
            cell.setCellValue("Tanggal Pengajuan");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(7);
            cell.setCellValue("Tanggal Pengajuan Diterima");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(8);
            cell.setCellValue("Tanggal Cicilan Selesai");
            cell.setCellStyle(headerCellStyle);

            int i = 8;
            List<Map<String, Object>> aktivasiSimpananList = (List<Map<String, Object>>) data.get("dataTable");
            for (Map<String, Object> dataTable : aktivasiSimpananList) {
                String pattern = "MM/dd/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(1).setCellValue(String.valueOf(dataTable.get("noTransaksi")));
                dataRow.createCell(2).setCellValue(String.valueOf(dataTable.get("nama")));
                dataRow.createCell(3).setCellValue(toIDR(((Double) dataTable.get("jumlahPinjaman")).intValue()));
                dataRow.createCell(4).setCellValue(toIDR(new Double((double) dataTable.get("laba")).intValue()));
                dataRow.createCell(5).setCellValue((int) dataTable.get("tenor") + " bulan");
                dataRow.createCell(6).setCellValue(df.format((Date) dataTable.get("tglPengajuan")));
                dataRow.createCell(7).setCellValue(df.format((Date) dataTable.get("tglDiterimaPengajuan")));
                dataRow.createCell(8).setCellValue(df.format((Date) dataTable.get("tglSelesaiCicilan")));
                i++;
            }


            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);
            sheet.autoSizeColumn(4);
            sheet.autoSizeColumn(5);
            sheet.autoSizeColumn(6);
            sheet.autoSizeColumn(7);
            sheet.autoSizeColumn(8);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ByteArrayInputStream downloadSimpanan(Map<String, Object> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Simpanan");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));

            Row rowHeader = sheet.createRow(1);
            CellStyle headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell1 = rowHeader.createCell(1);
            cell1.setCellValue(String.valueOf(data.get("namaKoperasi")));
            cell1.setCellStyle(headerCellStyle1);

            rowHeader = sheet.createRow(0);
            headerCellStyle1 = workbook.createCellStyle();
            headerCellStyle1.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle1.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell1 = rowHeader.createCell(1);
            cell1.setCellValue("Rekapitulasi Simpanan");
            cell1.setCellStyle(headerCellStyle1);

            Row row = sheet.createRow(4);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            Cell cell = row.createCell(1);
            cell.setCellValue("Simpanan Pokok");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("pokok")).intValue()));

            row = sheet.createRow(5);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(1);
            cell.setCellValue("Simpanan Wajib");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("wajib")).intValue()));

            row = sheet.createRow(6);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(1);
            cell.setCellValue("Simpanan Sukarela");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("sukarela")).intValue()));


            row = sheet.createRow(7);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(1);
            cell.setCellValue("Total Simpanan");
            cell.setCellStyle(headerCellStyle);
            row.createCell(2).setCellValue(toIDR(((Long) data.get("pokok")).intValue()
                    + ((Long) data.get("wajib")).intValue()
                    + ((Long) data.get("sukarela")).intValue()));

            row = sheet.createRow(9);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Creating header
            cell = row.createCell(0);
            cell.setCellValue("Nama Nasabah");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("Simpanan Pokok");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("Simpanan Wajib");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("Simpanan Sukarela");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(4);
            cell.setCellValue("Total");
            cell.setCellStyle(headerCellStyle);

//             Creating data rows for each customer
            int i = 9;
            List<Map<String, Object>> aktivasiSimpananList = (List<Map<String, Object>>) data.get("dataTable");
            for (Map<String, Object> dataTable : aktivasiSimpananList) {
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(String.valueOf(dataTable.get("name")));
                dataRow.createCell(1).setCellValue(toIDR(((Long) dataTable.get("pokok")).intValue()));
                dataRow.createCell(2).setCellValue(toIDR(((Long) dataTable.get("wajib")).intValue()));
                dataRow.createCell(3).setCellValue(toIDR(((Long) dataTable.get("sukarela")).intValue()));
                dataRow.createCell(4).setCellValue(toIDR(((Long) dataTable.get("pokok")).intValue() + ((Long) dataTable.get("wajib")).intValue() + ((Long) dataTable.get("sukarela")).intValue()));
                i++;
            }

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
