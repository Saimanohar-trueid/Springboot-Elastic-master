package com.elastic.trueid.model;
import java.util.List;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "QRATIO_MATCH_RESPONSE")
public class QratioRespModel {
	

	@Id
	Long uuid;
	String matchedName;
	String score;
	

}
