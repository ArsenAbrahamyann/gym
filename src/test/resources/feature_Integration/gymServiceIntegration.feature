Feature: GymService JMS Communication
  As a service
  I want to communicate with TrainerService via ActiveMQ
  So that I can update workloads and request training hours

  Background:
    Given the ActiveMQ broker is running

  Scenario: Send training update to trainer service
    When the gymService sends a training update message
    Then the trainer service should receive the training update

  Scenario: Request training hours from trainer service
    When the gymService sends a training hours request message
    Then the gymService should receive the training hours response