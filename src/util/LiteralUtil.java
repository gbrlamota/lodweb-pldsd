package util;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.datatypes.xsd.IllegalDateTimeFieldException;
import org.apache.jena.datatypes.xsd.XSDDateTime;

import node.SparqlWalk;

public class LiteralUtil
{	
	static Map<String, Map<EStatistic, Double>> statistics = new HashMap<String, Map<EStatistic, Double>>();

	public static synchronized Map<EStatistic, Double> getStatistics(String propertyUri)
	{
		Map<EStatistic, Double> propertyStatistics;

		if (statistics.containsKey(propertyUri))
			propertyStatistics = statistics.get(propertyUri);
		else
		{
			propertyStatistics = new HashMap<EStatistic, Double>();
			statistics.put(propertyUri, propertyStatistics);
		}

		return propertyStatistics;
	}
	
	public static synchronized double normalize(double value, String propertyUri)
	{
		return LiteralUtil.normalize(value, propertyUri, "");
	}
	
	public static synchronized double normalize(double value, String propertyUri, String type)
	{
		double maximumValue, minimumValue, standardDeviation;

		Map<EStatistic, Double> propertyStatistics = LiteralUtil.getStatistics(propertyUri+type);
		
		//if (propertyStatistics.containsKey(EStatistic.STANDARD_DEVIATION))
		//	standardDeviation = propertyStatistics.get(EStatistic.STANDARD_DEVIATION);
		//else
		//{
		//	double variance = SparqlWalk.getVarianceOfNumericProperty(propertyUri, type);
		//	standardDeviation = Math.sqrt(variance);
		//	propertyStatistics.put(EStatistic.STANDARD_DEVIATION, standardDeviation);
		//}
		
		if (propertyStatistics.containsKey(EStatistic.MAXIMUM))
			maximumValue = propertyStatistics.get(EStatistic.MAXIMUM);
		else
		{
			maximumValue = SparqlWalk.getExtremeValueOfNumericProperty(propertyUri, type, false);
			propertyStatistics.put(EStatistic.MAXIMUM, maximumValue);
		}

		if (propertyStatistics.containsKey(EStatistic.MINIMUM))
			minimumValue = propertyStatistics.get(EStatistic.MINIMUM);
		else
		{
			minimumValue = SparqlWalk.getExtremeValueOfNumericProperty(propertyUri, type, true);
			propertyStatistics.put(EStatistic.MINIMUM, minimumValue);
		}

		return (value - minimumValue) / (maximumValue - minimumValue);
	}

	public static synchronized double normalize(XSDDateTime value, String propertyUri)
	{
		double maximumValue, minimumValue;

		Map<EStatistic, Double> propertyStatistics = LiteralUtil.getStatistics(propertyUri);
		
		if (propertyStatistics.containsKey(EStatistic.MAXIMUM))
			maximumValue = propertyStatistics.get(EStatistic.MAXIMUM);
		else
		{
			maximumValue = SparqlWalk.getExtremeValueOfDateProperty(propertyUri, false); 
			propertyStatistics.put(EStatistic.MAXIMUM, maximumValue);
		}

		if (propertyStatistics.containsKey(EStatistic.MINIMUM))
			minimumValue = propertyStatistics.get(EStatistic.MINIMUM);
		else
		{
			minimumValue = SparqlWalk.getExtremeValueOfDateProperty(propertyUri, true);
			propertyStatistics.put(EStatistic.MINIMUM, minimumValue);
		}

		double years, months, days;
		try
		{
			years = value.getYears()*10000d;
		}
		catch(IllegalDateTimeFieldException ex)
		{
			years = 0d;
		}

		try
		{
			months = value.getMonths()*100d;
		}
		catch(IllegalDateTimeFieldException ex)
		{
			months = 0d;
		}

		try
		{
			days = Double.parseDouble(String.valueOf(value.getDays()));
		}
		catch(IllegalDateTimeFieldException ex)
		{
			days = 0d;
		}
		
		double currentValue = (Double) (years + months + days);
		return (currentValue - minimumValue) / (maximumValue - minimumValue); 
	}
}
