package com.github.nemojmenervirat.flightadvisor.utils;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.github.nemojmenervirat.flightadvisor.exception.CustomException;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ContextHolder;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseItemsContext;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseItemsResult;
import com.github.nemojmenervirat.flightadvisor.parsecsv.ParseProcessor;
import com.univocity.parsers.common.Context;
import com.univocity.parsers.common.processor.core.Processor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.UnescapedQuoteHandling;

public class FileUtils {

	public static boolean isTxtOrCsv(MultipartFile file) {
		return file.getContentType().equals("text/csv") || file.getContentType().equals("text/plain");
	}

	public static <C extends ParseItemsContext<T>, T> ParseItemsResult parseCsv(MultipartFile file, ParseProcessor<T, C> processor) {
		try (InputStream inputStream = file.getInputStream()) {
			return parseCsv(inputStream, processor);
		} catch (IOException ex) {
			throw new CustomException("I/O Exception: " + ex.getMessage());
		}
	}

	public static <C extends ParseItemsContext<T>, T> ParseItemsResult parseCsv(InputStream inputStream, ParseProcessor<T, C> processor) {
		CsvParserSettings settings = new CsvParserSettings();
		settings.setUnescapedQuoteHandling(UnescapedQuoteHandling.STOP_AT_CLOSING_QUOTE);
		ContextHolder<T, C> contextHolder = new ContextHolder<>();
		settings.setProcessor(new Processor<Context>() {

			@Override
			public void processStarted(Context context) {
				contextHolder.setContext(processor.processStarted());
			}

			@Override
			public void rowProcessed(String[] row, Context context) {
				processor.rowProcessed(row, contextHolder.getContext());
			}

			@Override
			public void processEnded(Context context) {
				processor.processEnded(contextHolder.getContext());
			}
		});

		CsvParser parser = new CsvParser(settings);
		parser.parse(inputStream);

		return ParseItemsResult.of(contextHolder.getContext().getImportedCount(), contextHolder.getContext().getIgnoredCount());
	}

}
