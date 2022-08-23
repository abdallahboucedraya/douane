package com.wintex.douane;

import java.awt.Dialog.ModalExclusionType;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class ServiceJasperReport {

	public static void exportToPdf(Record record) throws URISyntaxException, JRException {
		JasperReport jasperReport = getJasperReport();
		Map<String, Object> parameters = getParameters(record);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
		JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
		jasperViewer.setTitle("Convocation " + record.getFullName() + " - " + LocalDateTime.now());
		jasperViewer.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		jasperViewer.setVisible(true);
	}

	public static JasperReport getJasperReport() throws URISyntaxException, JRException {
		URL resource = ServiceJasperReport.class.getClassLoader().getResource("convocation.jrxml");
		File template = new File(resource.toURI());
		return JasperCompileManager.compileReport(template.getAbsolutePath());
	}

	public static Map<String, Object> getParameters(Record record) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("fullName", record.getFullName());
		parameters.put("address", record.getAddresse());
		return parameters;
	}

}
