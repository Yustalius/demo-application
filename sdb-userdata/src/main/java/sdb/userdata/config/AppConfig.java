package sdb.userdata.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@Configuration
@EnableWs
public class AppConfig {

  public static final String SOAP_NAMESPACE = "sdb-userdata";

  @Value("{sdb-userdata.base-uri}")
  private String sdbUserdataBaseUri;

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  @Bean(name = "userdata")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema userdataSchema) {

    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName("Sdb UserdataPort");
    wsdl11Definition.setLocationUri(sdbUserdataBaseUri + "/ws");
    wsdl11Definition.setTargetNamespace("sdb-userdata");
    wsdl11Definition.setSchema(userdataSchema);
    return wsdl11Definition;
  }

  @Bean
  public XsdSchema userdataSchema() {
    return new SimpleXsdSchema(new ClassPathResource("userdata.xsd"));
  }
}
