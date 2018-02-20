/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.stpapriv.util.jobs;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import xstampp.stpapriv.model.PrivacyController;

/**
 *
 * @author Lukas Balzer
 *
 */
public class STPALoadJob extends xstampp.astpa.util.jobs.STPALoadJob {

  @Override
  protected Schema getSchema() throws SAXException {
    InputStream schemaFile;
    schemaFile = getClass().getResourceAsStream("/secschema.xsd"); //$NON-NLS-1$
    InputStream mainSchemaFile = xstampp.astpa.Activator.class.getResourceAsStream("/hazschema.xsd"); //$NON-NLS-1$
    Source imp = new StreamSource(schemaFile);
    Source main = new StreamSource(mainSchemaFile);
    Source[] schemaFiles = new Source[] { main, imp };
    SchemaFactory schemaFactory = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    return schemaFactory.newSchema(schemaFiles);
  }

  @Override
  protected Class<?> getLoadModel() {
    return PrivacyController.class;
  }
}
