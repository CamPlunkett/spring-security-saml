/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.springframework.security.saml2.util;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.saml2.Namespace;
import org.w3c.dom.Node;
import org.xmlunit.xpath.JAXPXPathEngine;
import org.xmlunit.xpath.XPathEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlTestUtil {

    private static XPathEngine engine;

    static {
        engine = new JAXPXPathEngine();
        HashMap<String, String> nsContext = new HashMap<>();
        nsContext.put("md", Namespace.NS_METADATA);
        nsContext.put("ds", Namespace.NS_SIGNATURE);
        engine.setNamespaceContext(nsContext);
    }


    public static void assertNodeAttribute(Node node, String attribute, String expected) {
        Node attr = node.getAttributes().getNamedItem(attribute);
        assertEquals(expected, attr.getTextContent());
    }

    public static void assertNodeCount(String xml, String xPath, int expected) {
        Iterable<Node> nodes = getNodes(xml, xPath);
        AtomicInteger count = new AtomicInteger(0);
        nodes.forEach(p -> count.incrementAndGet());
        assertEquals(
            expected,
            count.get()
        );
    }

    public static Iterable<Node> getNodes(String xml, String xPath) {
        return engine
            .selectNodes(
                xPath,
                new StreamSource(
                    new StringReader(xml)
                )
            );
    }
}
