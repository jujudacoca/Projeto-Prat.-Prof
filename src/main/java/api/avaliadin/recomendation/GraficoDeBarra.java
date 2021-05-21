package api.avaliadin.recomendation;

import java.awt.Color;

import java.util.List;

import org.jfree.chart.ChartFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;

import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraficoDeBarra {

		public CategoryDataset createDataSet(List<Ufc> lista) {
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for(Ufc uf: lista) {
				dataset.addValue(uf.getContador(),"", uf.getUf());
			}
			return dataset;
		}
		
		public JFreeChart createBarChart(CategoryDataset dataset, int size) {
			JFreeChart graficoBarras = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
			CategoryPlot plot = graficoBarras.getCategoryPlot();
			plot.setBackgroundPaint(Color.white);
			plot.setDomainGridlinePaint(new Color(160, 163, 165)); 
			plot.setRangeGridlinePaint(new Color(160, 163, 165)); 
			plot.setOutlineVisible(false); 
			((BarRenderer) plot.getRenderer()).setBarPainter(new StandardBarPainter());
			graficoBarras.setBorderPaint(new Color(160, 163, 165));
			graficoBarras.setBorderVisible(true);
	
			plot.getRenderer().setSeriesPaint(0, new Color(255, 154, 31));
			graficoBarras.getLegend().setFrame(BlockBorder.NONE);
			graficoBarras.removeLegend();
			plot.getRenderer().setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
			plot.getRenderer().setDefaultItemLabelsVisible(true);
			plot.getRenderer().setDefaultItemLabelPaint(new Color(160, 163, 165));
			plot.getDomainAxis().setTickLabelPaint(new Color(160, 163, 165));
			plot.getDomainAxis().setCategoryMargin(0.2);
			plot.getDomainAxis().setCategoryLabelPositionOffset(4);
			return graficoBarras;
		}
		
}
