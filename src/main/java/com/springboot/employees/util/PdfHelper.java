package com.springboot.employees.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.springboot.employees.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PdfHelper {

	public static Font headerFont = new Font(FontFamily.TIMES_ROMAN, 14, Font.BOLD, new BaseColor(0, 0, 0));

	public static Font poppinsFontColor = FontFactory.getFont("Poppins", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14,
			Font.BOLD, new BaseColor(135, 206, 250));

	public static Font getPoppinsBoldFont(int size, BaseColor color) {
		if(color == null) {
			color = new BaseColor(0,0,0); //default as black colour
		}
		return FontFactory.getFont("Poppins", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, size, Font.BOLD, color);
	}
	
	public static Font getPoppinsFont(int size, BaseColor color) {
		if(color == null) {
			color = new BaseColor(0,0,0); //default as black colour
		}
		return FontFactory.getFont("Poppins", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, size, Font.NORMAL, color);
	}

	public static Font getFont(int size, int style, BaseColor color) {
		return new Font(FontFamily.TIMES_ROMAN, size, style, color);
	}

	public static Image createImage(String filename) {
		Image image;
		try {
			// Create an Image object
			image = Image.getInstance(filename);
			image.setAlignment(Image.ALIGN_CENTER);
			// Set position and size of the image
			image.scalePercent(30f);
		} catch (Exception e) {
			log.error("error in createImage", e);
			throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return image;
	}

	public static PdfPTable createTable(int size, float spacingBefore, float spacingAfter, int width) {
		PdfPTable table = new PdfPTable(size);
		table.setSpacingBefore(spacingBefore);
		table.setSpacingAfter(spacingAfter);
		table.setWidthPercentage(width);
		return table;
	}

	public static PdfPTable createNoBorderTable(int size, float spacingBefore, float spacingAfter, int width) {
		PdfPTable table = new PdfPTable(size);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setSpacingBefore(spacingBefore);
		table.setSpacingAfter(spacingAfter);
		table.setWidthPercentage(width);
		return table;
	}

	public static Paragraph createParagraph(String title, int spacingBefore, int spacingAfter,
			Font font) {
		Paragraph paragraph = new Paragraph(title, font);
		paragraph.setSpacingBefore(spacingBefore);
		paragraph.setSpacingAfter(spacingAfter);
		return paragraph;
	}

	public static void headerCell(PdfPTable table, String text,BaseColor backgroundColor,Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
//		cell.setFixedHeight(32f);
		cell.setPaddingTop(5);
		cell.setPaddingBottom(5);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(backgroundColor);
		table.addCell(cell);
	}

	public static void noBorderCell(PdfPTable table, String text,Font font,int pading,int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
		cell.setPaddingTop(pading);
		cell.setPaddingBottom(pading);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(alignment);
		table.addCell(cell);
	}

	public static void noBorderCell(PdfPTable table, String text,Font font,int pading,int alignment, int colspan,int paddingLeft) {
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setColspan(colspan);
		cell.setPaddingTop(pading);
		cell.setPaddingBottom(pading);
		cell.setPaddingLeft(paddingLeft);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(alignment);
		table.addCell(cell);
	}

	public static void createPdfPCell(PdfPTable table, String content, Font font, int padding,
			int horizontalAlignment) {
		PdfPCell cell = new PdfPCell(new Phrase(content != null ? content : "", font));
		cell.setPaddingTop(padding);
		cell.setPaddingBottom(padding);
		cell.setHorizontalAlignment(horizontalAlignment);
		table.addCell(cell);
	}

	public static void createPdfPCell(PdfPTable table, String content, Font font, int padding, int paddingLeft,
			int horizontalAlignment) {
		PdfPCell cell = new PdfPCell(new Phrase(content != null ? content : "", font));
		cell.setPaddingTop(padding);
		cell.setPaddingBottom(padding);
		cell.setPaddingLeft(paddingLeft);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(horizontalAlignment);
		table.addCell(cell);
	}

	public static String numberConvert(Long number) {
		return number != null ? number.toString() : "";
	}

	public static String numberConvert(BigDecimal number) {
		return number != null ? number.toString() : "";
	}

	public static String dateConvert(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		return date != null ? date.format(formatter) : "";
	}

	public static String numberFormatGrid(BigDecimal number) {

		if (number != null && number.compareTo(BigDecimal.ZERO) > 0) {
			return new DecimalFormat("$ #,##0.00").format(number);
		} else if (number != null && number.compareTo(BigDecimal.ZERO) < 0) {
			number = number.abs();
			return new DecimalFormat("$( #,##0.00 )").format(number);
		} else {
			return "$ 0.00";
		}

	}
	
	public static String numberFormat(Long number) {
		return number != null ? new DecimalFormat("$ #,##0").format(number) : "$ 0.00";
	}
	
	public static void createPdfPCell(PdfPTable table, LineSeparator ls) {
		PdfPCell cell = new PdfPCell();
		ls.setLineColor(BaseColor.BLACK);
		cell.addElement(new Chunk(ls));
		cell.setPaddingTop(0);
		cell.setPaddingBottom(10);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);
	}
	
}
