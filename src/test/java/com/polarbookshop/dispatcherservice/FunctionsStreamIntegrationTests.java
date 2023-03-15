package com.polarbookshop.dispatcherservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class FunctionsStreamIntegrationTests {

  @Autowired
  private InputDestination input;

  @Autowired
  private OutputDestination output;

  @Autowired
  private ObjectMapper objectMapper;


  @Test
  void whenOrderAcceptedThenDispatched() throws IOException {
    long orderId = 12345;
    var inputMessage = MessageBuilder.withPayload(new OrderAcceptedMessage(orderId)).build();
    var expectedOutputMessage = MessageBuilder.withPayload(new OrderDispatchedMessage(orderId)).build();

    this.input.send(inputMessage);
    assertThat(objectMapper.readValue(output.receive().getPayload(), OrderDispatchedMessage.class))
        .isEqualTo(expectedOutputMessage.getPayload());
  }

}
