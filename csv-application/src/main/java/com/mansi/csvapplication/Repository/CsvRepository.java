package com.mansi.csvapplication.Repository;

import com.mansi.csvapplication.Model.CsvModel;
import org.springframework.data.jpa.repository.JpaRepository;
//jpa repository is pre defined class to perform all crud operations to perform basic sql operations

import org.springframework.stereotype.Repository;

@Repository
public interface CsvRepository extends JpaRepository<CsvModel, Integer>{
}