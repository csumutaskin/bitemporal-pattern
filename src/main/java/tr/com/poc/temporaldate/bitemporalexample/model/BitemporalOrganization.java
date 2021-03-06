package tr.com.poc.temporaldate.bitemporalexample.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import tr.com.poc.temporaldate.core.dao.annotation.Pid;
import tr.com.poc.temporaldate.core.model.bitemporal.BaseBitemporalEntity;

/**
 * BitemporalOrganization Entity
 * 
 * @author umutaskin
 */

@SuppressWarnings("serial")
@Table(name = "BT_ORGANIZATION")
@Entity
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Setter 
@Builder
@ToString(callSuper=true, includeFieldNames=true)
@Where(clause = "IS_DELETED = 'FALSE'" )
@SQLDelete(sql = "UPDATE BT_ORGANIZATION SET IS_DELETED = 'TRUE' WHERE id = ?", check = ResultCheckStyle.COUNT)
public class BitemporalOrganization extends BaseBitemporalEntity
{
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "ORG_ID")
	@Pid(sequenceName="ORGANIZATION_PID_SEQUENCE")
	private Long orgId;
	
	@Column(name = "FINE_AMOUNT")
	private double fineAmount;
	
	@Column(name = "EARN_AMOUNT")
	private double earnAmount;
}
