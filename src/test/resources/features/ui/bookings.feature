@ui
Feature: Table Booking

  @smoke @regression
  Scenario Outline: User sees the available time slots for booking
    Given the user is on the table booking page
    When the user books a table with location "<location>", date "<date>", time "<time>", and guest count <guest_count>
    And the user submits the booking form
    Then available tables should be displayed

    Examples:
      | location                  | date       | time  | guest_count |
      | 456 Ocean Avenue, Seaside | 2026-06-20 | 17:30 | 4           |
      | 123 Main Street, Downtown | 2025-09-01 | 20:00 | 3           |
      | 789 Sunset Blvd, Midtown  | 2026-07-20 | 23:00 | 6           |
      | 789 Sunset Blvd, Midtown  | 2025-07-20 | 23:00 | 2           |

  @smoke @regression
  Scenario: User books a table from the available time slots
    Given the user is logged in and is on the table booking page
    When The user selects an available time slot and books it
    Then The reservation should be successfully made
    And The reservation should be displayed in reservations

  Scenario: CleanUp
    Given The user wants to cleanup all the reservation
