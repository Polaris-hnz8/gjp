package com.itcast.tools;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.util.Map;

public class JFreeChartUtils {
	/**
	 * 使用jfreechart生成图片
	 * @param title 标题
	 * @param totalMap 数据（key:分类名称, value：该分类的汇总数据，例如分类名称为"工资收入"，汇总数据为所有工资收入的和）
	 * @param sum 汇总数据和（例如"工资收入汇总 + 股票收入汇总 + ...."）
	 * @param path 生成图片保存路径
	 */
	public static void pie(String title, Map<String,Double> totalMap, double sum, String path) {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		for(String dataName : totalMap.keySet()) {
			double dataValue = totalMap.get(dataName);
			String bf = String.format("%.2f%%", dataValue / sum * 100);
			dataName = dataName + ":" + dataValue + "元(" + bf + ")";
			pieDataset.setValue(dataName, dataValue);
		}
		JFreeChart chart = ChartFactory.createPieChart3D(title, pieDataset, true, true, false);
		
		try {
			ChartUtils.saveChartAsJPEG(new File(path), chart, 500, 300);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void Ring(String title, Map<String,Double> totalMap, double sum, String path) {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		for(String dataName : totalMap.keySet()) {
			double dataValue = totalMap.get(dataName);
			String bf = String.format("%.2f%%", dataValue / sum * 100);
			dataName = dataName + ":" + dataValue + "元(" + bf + ")";
			pieDataset.setValue(dataName, dataValue);
		}
		JFreeChart Ringchart=ChartFactory.createRingChart(title, pieDataset, true, true, false);

		try {
			ChartUtils.saveChartAsJPEG(new File(path), Ringchart, 500, 300);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void barGraph(String title, Map<String,Double> totalMap, double sum, String path){
//		DefaultPieDataset pieDataset = new DefaultPieDataset();
//		for(String dataName : totalMap.keySet()) {
//			double dataValue = totalMap.get(dataName);
//			String bf = String.format("%.2f%%", dataValue / sum * 100);
//			dataName = dataName + ":" + dataValue + "元(" + bf + ")";
//			pieDataset.setValue(dataName, dataValue);
//		}
		//1.创建分类数据集
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for(String dataName : totalMap.keySet()) {
			double dataValue = totalMap.get(dataName);
			String bf = String.format("%.2f%%", dataValue / sum * 100);
			dataName = dataName + ":" + dataValue + "元(" + bf + ")";
			dataset.setValue(dataValue, dataName, dataName);
		}

		//JFreeChart chart=ChartFactory.createPieChart(title, pieDataset, true, true, false);
		//2.关键数据内容dataset
		JFreeChart Ringchart = ChartFactory.createBarChart(title,"横轴", "纵轴",dataset, PlotOrientation.VERTICAL, true, true, true);

		try {
			ChartUtils.saveChartAsJPEG(new File(path), Ringchart, 500, 300);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}

	}

}
