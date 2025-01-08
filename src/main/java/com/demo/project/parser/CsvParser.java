package com.demo.project.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.project.model.LostItem;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvRuntimeException;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CsvParser implements LostItemParser<InputStream> {

  @Override
  public List<LostItem> parse(InputStream input) {
    List<LostItem> lostItems = new ArrayList<>();

    try (input;
        InputStreamReader inputStreamReader = new InputStreamReader(input);
        CSVReader csvReader = new CSVReader(inputStreamReader)) {

      String[] line;
      while ((line = csvReader.readNext()) != null) {
        if (line.length != 3) {
          throw new IllegalArgumentException(String.format("Invalid length in CSV on line: %s", Arrays.toString(line)));
        }
        addLostItem(lostItems, line);
      }
    } catch (RuntimeException e) {
      log.error("Error processing file.", e);
      throw new CsvRuntimeException(e.getMessage(), e);
    } catch (IOException | CsvValidationException e) {
      log.error("Error reading file.", e);
      throw new CsvRuntimeException(e.getMessage(), e);
    }
    return lostItems;
  }

  private void addLostItem(List<LostItem> lostItems, String[] line) {
    try {
      lostItems.add(LostItem.builder()
          .itemName(line[0])
          .quantity(Integer.parseInt(line[1]))
          .place(line[2])
          .build());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(String.format("Invalid quantity in CSV on line: %s", Arrays.toString(line)));
    }
  }

}
