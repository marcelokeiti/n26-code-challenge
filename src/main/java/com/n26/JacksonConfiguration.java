package com.n26;

import java.time.ZonedDateTime;

import javax.money.MonetaryAmount;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.n26.infra.MonetaryAmountDeserializer;
import com.n26.infra.ZonedDateTimeDeserializer;

@Configuration
public class JacksonConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
	MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();

	jsonConverter.setObjectMapper(objectMapper());

	return jsonConverter;
    }

    @Bean
    public ObjectMapper objectMapper() {
	ObjectMapper mapper = new ObjectMapper();
	mapper.setSerializationInclusion(Include.NON_NULL);

	mapper.registerModule(new ParameterNamesModule());
	mapper.registerModule(new Jdk8Module());
	mapper.registerModule(new JavaTimeModule());

	SimpleModule module = new SimpleModule();
	module.addDeserializer(MonetaryAmount.class, new MonetaryAmountDeserializer());
	module.addDeserializer(ZonedDateTime.class, new ZonedDateTimeDeserializer());
	mapper.registerModule(module);

	return mapper;
    }

}
