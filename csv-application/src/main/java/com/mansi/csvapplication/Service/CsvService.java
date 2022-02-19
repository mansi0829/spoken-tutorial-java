package com.mansi.csvapplication.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.mansi.csvapplication.CsvHelper;
import com.mansi.csvapplication.Model.CsvModel;
import com.mansi.csvapplication.Repository.CsvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CsvService {
    @Autowired
    CsvRepository repository;

    public void save(MultipartFile file) {      //to store the file in mysql db after importing
        try {
            List<CsvModel> userInfo = CsvHelper.csvTodata(file.getInputStream());
            repository.saveAll(userInfo);                   //jpa repository used to save all data(file data) in db
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public ByteArrayInputStream load() {                //to load data from mysql to csv
        List<CsvModel> userInfo = repository.findAll();

        ByteArrayInputStream in = CsvHelper.dataTocsv(userInfo);
        return in;                                      //returning data in the form of byte array
    }

    public List<CsvModel> getAllUser() {                //to return data from mysql in the form of list(json format)
        return repository.findAll();
    }
}