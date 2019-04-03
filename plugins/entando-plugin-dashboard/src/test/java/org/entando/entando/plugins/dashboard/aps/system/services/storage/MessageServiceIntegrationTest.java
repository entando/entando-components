package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.junit.Test;
import org.springframework.data.domain.Page;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageServiceIntegrationTest extends DashboardBaseTestCase {

	private IMessageService messageService;
	private ObjectMapper mapper = new ObjectMapper();

	private static final int SERVER_ID = 123456;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.resetData();
	}

	private void init() {
		this.messageService = (IMessageService) this.getService("messageService");
	}

	private void resetData() {
		this.messageService.deleteByServerId(SERVER_ID);
	}

	@Test
	public void testSave() throws JsonProcessingException {
		Page<IotMessage> searchResult = this.messageService.findAll(null);
		assertThat(searchResult, is(not(nullValue())));
		assertThat(searchResult.getTotalElements(), is(0L));

		this.messageService.deleteByServerId(SERVER_ID);
		IotMessageDto message = new IotMessageDto(SERVER_ID, "mars", null);
		message.setContent(new SimpleMessagePayload(mapper.writeValueAsString("data_from_mars")));
		this.messageService.add(message);
		assertThat( message.getId(), is(not(nullValue())));

		searchResult = this.messageService.findAll(null);
		assertThat(searchResult, is(not(nullValue())));
		assertThat(searchResult.getTotalElements(), is(1L));
	}


	@Test
	public void testSearch() throws JsonProcessingException {
		String dashBoardCode = "mars";
		ZonedDateTime created = LocalDate
				.of(2017, 2, 16)
				.atTime(20, 22, 28)
				.atZone(ZoneId.of("CET"));


		long itemsCount = 300;
		for (int i = 0; i < itemsCount; i++) {
			created = created.plus(12, ChronoUnit.HOURS);
			IotMessageDto message = new IotMessageDto(SERVER_ID, dashBoardCode, null);
			message.setCreatedAt(created.toInstant());
			String content = String.format("payload_%s_%s", 
					message.getServerId(), 
					message.getDashboardCode()
					);
			message.setContent(new SimpleMessagePayload(mapper.writeValueAsString(content)));
			this.messageService.add(message);
		}

		Page<IotMessage> searchResult = this.messageService.findAll(null);
		assertThat(searchResult, is(not(nullValue())));
		assertThat(searchResult.getTotalElements(), is(itemsCount));



		ZonedDateTime start = LocalDate
				.of(2017, 3, 1)
				.atTime(8, 0, 0)
				.atZone(ZoneId.of("CET"));

		ZonedDateTime end = LocalDate
				.of(2017, 3, 2)
				.atTime(8, 0, 0)
				.atZone(ZoneId.of("CET"));

		Date.from(start.toInstant());

		searchResult =this.messageService.findByServerConfigurationAndDate(SERVER_ID, dashBoardCode, Date.from(start.toInstant()), Date.from(end.toInstant()),  null);
		assertThat(searchResult.getTotalElements(), is(2L));

	}
}
