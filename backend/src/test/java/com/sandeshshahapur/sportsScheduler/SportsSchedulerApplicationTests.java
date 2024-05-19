package com.sandeshshahapur.sportsScheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandeshshahapur.sportsScheduler.Model.SportsScheduleRequest;
import com.sandeshshahapur.sportsScheduler.controller.SportsSchedulerController;
import com.sandeshshahapur.sportsScheduler.service.SportsSchedulerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@WebMvcTest(SportsSchedulerController.class)
public class SportsSchedulerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SportsSchedulerService scheduleService;

	@Test
	public void testGenerateSchedule() throws Exception {
		SportsScheduleRequest request = new SportsScheduleRequest();
		request.setTeams(new String[] { "Thunder", "Titans", "Raptors", "Fire", "water", "Rock" });
		request.setMatchesBtwTeams(2);
		request.setDayNumMatches(new long[] {1, 2});
		request.setTeamMatchesGap(1);
		request.setTeamDaysGap(1);
		request.setCurDate("2024-05-17");
		request.setAlwEmtDay(false);
		request.setAlwTeamPairRecur(false);
		request.setOpSchedCnt(2);

		mockMvc.perform(MockMvcRequestBuilders.post("/generateStandardSchedule")
				.content(new ObjectMapper().writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
