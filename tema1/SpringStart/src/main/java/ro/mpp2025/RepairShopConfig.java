package ro.mpp2025;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ro.mpp2025.repository.ComputerRepairRequestRepository;
import ro.mpp2025.repository.ComputerRepairedFormRepository;
import ro.mpp2025.repository.file.ComputerRepairRequestFileRepository;
import ro.mpp2025.repository.file.ComputerRepairedFormFileRepository;
import ro.mpp2025.repository.jdbc.ComputerRepairRequestJdbcRepository;
import ro.mpp2025.repository.jdbc.ComputerRepairedFormJdbcRepository;
import ro.mpp2025.services.ComputerRepairServices;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class RepairShopConfig {
    @Bean
    Properties getProps() {
        Properties properties = new Properties();
        try{
            System.out.println("Searching bd.config in directory" + (new File(".").getAbsolutePath()));
            properties.load(new FileReader("bd.config"));
//            properties.load(new FileReader("/ComputerRequests.txt"));
        }catch (IOException e){
            e.printStackTrace();
            System.err.println("bd.config file not found" + e);
        }
        return properties;
    }

    @Bean
    ComputerRepairRequestRepository requestsRepo(){
        return new ComputerRepairRequestJdbcRepository(getProps());
//        return new ComputerRepairRequestFileRepository(getProps().toString());
    }

    @Bean
    ComputerRepairedFormRepository formsRepo(){
        return new ComputerRepairedFormJdbcRepository(getProps());
//        return new ComputerRepairedFormFileRepository("/RepairedForms.txt", requestsRepo());
    }

    @Bean
    ComputerRepairServices services(){
       return new ComputerRepairServices(requestsRepo(), formsRepo());
    }

}
