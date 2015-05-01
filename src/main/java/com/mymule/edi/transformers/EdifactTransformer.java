package com.mymule.edi.transformers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamSource;

import org.milyn.Smooks;
import org.milyn.SmooksException;
import org.milyn.payload.StringResult;
import org.milyn.smooks.edi.unedifact.UNEdifactReaderConfigurator;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.xml.sax.SAXException;

public class EdifactTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		String messageOut;
		try {
			messageOut = transformEdi(message);
			message.setPayload(messageOut);
			return message;
		} catch (SmooksException e) {
			//handle exception
		} catch (IOException e) {
			//handle exception
		} catch (SAXException e) {
			//handle exception
		}
		return null;

	}

	protected String transformEdi(MuleMessage message)
			throws IOException, SAXException, SmooksException {

		Smooks smooks = null;
		try {

			StringResult result = new StringResult();
			// Instantiate Smooks with the config...
			smooks = new Smooks("smooks-config.xml");
			smooks.setReaderConfig(new UNEdifactReaderConfigurator(
					"urn:org.milyn.edi.unedifact:d98b-mapping:*"));
			smooks.filterSource(new StreamSource(new ByteArrayInputStream(
					message.getPayloadAsBytes())), result);
			return result.getResult();
		} catch (Exception e) {
			// handle exception
		} finally {
			if (smooks != null) {
				smooks.close();
			}
		}
		return null;

	}

}
