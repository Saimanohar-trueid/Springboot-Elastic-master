package com.elastic.service;

import java.util.List;

import com.elastic.trueid.entity.AMLWorldCheckCaseData;
import com.elastic.trueid.entity.AMLWorldCheckCaseManagment;





public interface AMLCaseManagmentService {

	String amlCreateCase();
	
	List<AMLWorldCheckCaseData> amlUpdateCase(List<AMLWorldCheckCaseData> amlCaseDetails);
	
	AMLWorldCheckCaseManagment amlUpdateCaseMange(AMLWorldCheckCaseManagment amlMangCaseDetails);
	
	List<AMLWorldCheckCaseManagment> amlGetAllCases();
	
	List<AMLWorldCheckCaseManagment> amlGetByCaseStatus(String caseStatus);
	
	List<AMLWorldCheckCaseData> amlGetCaseById(Long caseId);
}
