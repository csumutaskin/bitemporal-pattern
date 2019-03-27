package tr.com.poc.temporaldate.core.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @Builder
public class BusinessException extends BaseException 
{
	private Long exceptionCode;
	private String exceptionMessage;
	//private String exceptionLogMessage;
	private Exception causeException;
}
