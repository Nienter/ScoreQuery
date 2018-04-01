package com.mike.scorequery.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//direction1 ����   temperature1�¶� city  status1 status2����
public class MyContentHandler extends DefaultHandler {

	String tagName;
	String city;
	String status1, status2;
	String temperature1, temperature2;
	
	@Override
	public void startDocument() throws SAXException {
		System.out.println("-------------��ʼ����--------------");
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		tagName = localName;
		//System.out.println(tagName);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (tagName.equals("city")) {
			city = new String(ch, start, length);
		}
		if (tagName.equals("status1")) {
			status1 = new String(ch, start, length);
		}
		if (tagName.equals("status2")) {
			status2 = new String(ch, start, length);
		}
		if (tagName.equals("temperature1")) {
			temperature1 = new String(ch, start, length);
		}
		if (tagName.equals("temperature2")) {
			temperature2 = new String(ch, start, length);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//�˴�����д��� tagName ���м�ΪlocalName
		if (localName.equals("Weather")) {
			
			System.out.println(city);
			System.out.println(status1);
			System.out.println(status2);
			System.out.println(temperature1);
			System.out.println(temperature2);
			
		}
	}

	@Override
	public void endDocument() throws SAXException {
		//System.out.println("-------------��������-------------");
	}
	

	public String getCity() {
		return city;
	}


	public String getStatus1() {
		return status1;
	}


	public String getStatus2() {
		return status2;
	}


	public String getTemperature1() {
		return temperature1;
	}


	public String getTemperature2() {
		return temperature2;
	}
}
