package com.dasmo.drs_api.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.dasmo.drs_api.model.Sequence;
import com.dasmo.drs_api.model.SequenceId;
import com.dasmo.drs_api.repo.SequenceRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SequenceService {

	private final SequenceRepo sequenceRepo;

	/*
	 * @param prefix
	 */
	public String getNewKey(String prefix) {
		Sequence newSequence = getNextSequence(prefix);
		String lastTwo = String.valueOf(newSequence.getYear()).substring(2);
		int lastTwoDigitOfYear = Integer.valueOf(lastTwo);
		String cleanedPrefix = "".equals(newSequence.getPrefix()) ? "" : newSequence.getPrefix() + "-";
		return String.format("%s%02d%02d-%03d"
				, cleanedPrefix
				, newSequence.getMonth()
				, lastTwoDigitOfYear
				, newSequence.getLastNumber());
	}

	@Transactional
	private Sequence getNextSequence(String prefix) {
		LocalDate date = LocalDate.now();
		int year = date.getYear();
		int month = date.getMonthValue();

		// get last number on records
		Integer lastNumber = sequenceRepo.findLastBySequenceId(prefix, year, month);

		int nextNumber = lastNumber + 1;
		SequenceId seqId = new SequenceId(prefix, year, month);

		Sequence sequence = sequenceRepo.findById(seqId).orElseGet(() -> {
			Sequence newSeq = new Sequence();
			newSeq.setPrefix(prefix);
			newSeq.setYear(year);
			newSeq.setMonth(month);
			return newSeq;
		});

		sequence.setLastNumber(nextNumber);
		sequenceRepo.save(sequence);

		return sequence;

	}

}
