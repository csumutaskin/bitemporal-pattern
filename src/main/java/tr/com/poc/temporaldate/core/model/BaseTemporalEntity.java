package tr.com.poc.temporaldate.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@NoArgsConstructor 
@AllArgsConstructor 
@Getter 
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTemporalEntity implements BaseEntity 
{
	@Column(name = "CREATE_USER")
	@CreatedBy
	private String createUser; 
	
	@Column(name = "IP")
	private String clientIp;

	@Column(name = "MODIFY_USER")
	@LastModifiedBy
	private String modifyUser;
	
	@Column(name = "CREATE_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate	
	private Date createDate;
	
	@Column(name = "MODIFY_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate	
	private Date modifyDate;
}
