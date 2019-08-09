package com.internship.tmontica_admin.menu;

import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MenuTestConfigration {

    @Bean
    public ModelMapper modelMapper(){	return new ModelMapper(); }

}
