package com.demo.project.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.demo.project.model.LostItem;
import com.demo.project.utils.LostItemUtils;
import com.opencsv.exceptions.CsvRuntimeException;

public class CsvParserTest {

  private CsvParser csvParser;

  @BeforeEach
  public void setup() {
    csvParser = new CsvParser();
  }

  @DisplayName("Should process one or more lines")
  @ParameterizedTest
  @MethodSource("randomLostItems")
  public void shouldProcessOneOrMoreLines(List<LostItem> inputLostItems) throws IOException {
    String csvFormat = "%s,%d,%s";
    List<String> csv = new ArrayList<>();

    inputLostItems.forEach(lostItem -> {
      csv.add(String.format(csvFormat, lostItem.getItemName(), lostItem.getQuantity(), lostItem.getPlace()));
    });

    InputStream inputStream = spy(new ByteArrayInputStream(String.join("\n", csv).getBytes()));

    List<LostItem> resultLostItems = csvParser.parse(inputStream);
    verify(inputStream, atLeastOnce()).close();
    assertThat(resultLostItems).isNotEmpty().hasSize(inputLostItems.size());

    for (int i = 0; i < inputLostItems.size(); i++) {
      LostItem resultLostItem = resultLostItems.get(i);
      assertThat(resultLostItem.getItemName()).isNotNull().isEqualTo(inputLostItems.get(i).getItemName());
      assertThat(resultLostItem.getQuantity()).isPositive().isEqualTo(inputLostItems.get(i).getQuantity());
      assertThat(resultLostItem.getPlace()).isNotNull().isEqualTo(inputLostItems.get(i).getPlace());
    }
  }

  @DisplayName("Should process empty CSV")
  @Test
  public void shouldProcessEmptyCSV() throws IOException {
    InputStream inputStream = spy(new ByteArrayInputStream("".getBytes()));

    List<LostItem> resultLostItems = csvParser.parse(inputStream);
    verify(inputStream, atLeastOnce()).close();
    assertThat(resultLostItems).isEmpty();
  }

  @DisplayName("Should not process invalid CSV")
  @MethodSource("invalidCSV")
  @ParameterizedTest
  public void shouldNotProcessInvalidCSV(String csv) throws IOException {
    InputStream inputStream = spy(new ByteArrayInputStream(csv.getBytes()));

    assertThatThrownBy(() -> {
      csvParser.parse(inputStream);
    }).isInstanceOf(CsvRuntimeException.class);
    
    verify(inputStream, atLeastOnce()).close();
  }

  static Stream<List<LostItem>> randomLostItems() {
    return Stream.of(
        Arrays.asList(createLostItem()),
        Arrays.asList(createLostItem(), createLostItem()),
        Arrays.asList(createLostItem(), createLostItem(), createLostItem(), createLostItem(),
            createLostItem()));
  }

  static Stream<String> invalidCSV() {
    return Stream.of(",", ",,", ",,,", "abc,abc,abc");
  }

  private static LostItem createLostItem() {
    return LostItemUtils.lostItemWithoutClaimedBy();
  }

}
