package com.juanm.backendapi.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class PdfService {

    public String extraerTextoDePdf(MultipartFile archivo) {
        try {
            byte[] bytesPdf = archivo.getBytes();
            try (PDDocument documentoPdf = Loader.loadPDF(bytesPdf)) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(documentoPdf);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al extraer texto del PDF: " + e.getMessage(), e);
        }
    }
}