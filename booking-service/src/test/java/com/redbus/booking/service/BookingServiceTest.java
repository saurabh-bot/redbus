package com.redbus.booking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redbus.booking.constants.BookingConstants;
import com.redbus.booking.dto.*;
import com.redbus.booking.exception.BookingNotFoundException;
import com.redbus.booking.exception.InvalidBookingStatusException;
import com.redbus.booking.exception.TripDetailsSerializationException;
import com.redbus.booking.model.Booking;
import com.redbus.booking.model.BookingPassenger;
import com.redbus.booking.model.enums.BookingStatus;
import com.redbus.booking.repository.BookingRepository;
import com.redbus.booking.repository.BookingPassengerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingPassengerRepository bookingPassengerRepository;

    @Mock
    private BookingReferenceGenerator bookingReferenceGenerator;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private BookingService bookingService;

    private CreateBookingRequest createRequest;
    private Booking savedBooking;
    private List<BookingPassenger> passengers;

    @BeforeEach
    void setUp() throws Exception {
        createRequest = CreateBookingRequest.builder()
                .userId(100L)
                .tripId(200L)
                .boardingCityName("Delhi")
                .droppingCityName("Mumbai")
                .boardingStopSequence(1)
                .droppingStopSequence(5)
                .boardingDatetime("2025-11-05T10:00:00")
                .droppingDatetime("2025-11-05T22:00:00")
                .totalAmount(1500.0)
                .passengers(Arrays.asList(
                        CreateBookingRequest.PassengerRequest.builder()
                                .seatNumber("A1")
                                .name("John Doe")
                                .age(30)
                                .gender("MALE")
                                .email("john@example.com")
                                .phoneNumber("9876543210")
                                .build()
                ))
                .build();

        String tripDetailsJson = "{\"trip_id\":\"200\",\"boarding_city_name\":\"Delhi\",\"dropping_city_name\":\"Mumbai\",\"booked_from_stop_sequence\":1,\"booked_to_stop_sequence\":5,\"boarding_datetime\":\"2025-11-05T10:00:00\",\"dropping_datetime\":\"2025-11-05T22:00:00\"}";

        savedBooking = Booking.builder()
                .bookingId(1L)
                .bookingReference("REDBUS20251105ABC")
                .userId(100L)
                .status(BookingStatus.INITIATED)
                .tripDetails(tripDetailsJson)
                .totalAmount(1500.0)
                .createdAtEpoch(Instant.now().getEpochSecond())
                .build();

        passengers = Arrays.asList(
                BookingPassenger.builder()
                        .passengerId(1L)
                        .bookingId(1L)
                        .seatNumber("A1")
                        .name("John Doe")
                        .age(30)
                        .gender("MALE")
                        .email("john@example.com")
                        .phoneNumber("9876543210")
                        .build()
        );

    }

    @Test
    void testCreateBooking_Success() throws Exception {
        String tripDetailsJson = "{\"trip_id\":\"200\",\"boarding_city_name\":\"Delhi\",\"dropping_city_name\":\"Mumbai\",\"booked_from_stop_sequence\":1,\"booked_to_stop_sequence\":5,\"boarding_datetime\":\"2025-11-05T10:00:00\",\"dropping_datetime\":\"2025-11-05T22:00:00\"}";
        
        when(bookingReferenceGenerator.generateBookingReference()).thenReturn("REDBUS20251105ABC");
        when(bookingRepository.findByBookingReference("REDBUS20251105ABC")).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setBookingId(1L);
            booking.setCreatedAtEpoch(Instant.now().getEpochSecond());
            booking.setTripDetails(tripDetailsJson);
            return booking;
        });
        when(bookingPassengerRepository.findByBookingId(1L)).thenReturn(passengers);
        when(objectMapper.writeValueAsString(any())).thenReturn(tripDetailsJson);
        when(objectMapper.readValue(eq(tripDetailsJson), eq(java.util.Map.class))).thenReturn(createTripDetailsMap());
        
        BookingResponse response = bookingService.createBooking(createRequest);

        assertNotNull(response);
        assertEquals("REDBUS20251105ABC", response.getBookingReference());
        assertEquals(100L, response.getUserId());
        assertEquals(200L, response.getTripId());
        assertEquals(BookingStatus.INITIATED, response.getStatus());
        assertEquals(1500.0, response.getTotalAmount());
        assertEquals(1, response.getPassengers().size());

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());
        Booking capturedBooking = bookingCaptor.getValue();
        assertEquals(BookingStatus.INITIATED, capturedBooking.getStatus());
        assertEquals("REDBUS20251105ABC", capturedBooking.getBookingReference());

        ArgumentCaptor<List<BookingPassenger>> passengersCaptor = ArgumentCaptor.forClass(List.class);
        verify(bookingPassengerRepository).saveAll(passengersCaptor.capture());
        List<BookingPassenger> capturedPassengers = passengersCaptor.getValue();
        assertEquals(1, capturedPassengers.size());
        assertEquals("A1", capturedPassengers.get(0).getSeatNumber());
    }

    @Test
    void testCreateBooking_WithDefaultAmount() throws Exception {
        createRequest.setTotalAmount(null);
        String tripDetailsJson = "{\"trip_id\":\"200\",\"boarding_city_name\":\"Delhi\",\"dropping_city_name\":\"Mumbai\",\"booked_from_stop_sequence\":1,\"booked_to_stop_sequence\":5,\"boarding_datetime\":\"2025-11-05T10:00:00\",\"dropping_datetime\":\"2025-11-05T22:00:00\"}";
        
        when(bookingReferenceGenerator.generateBookingReference()).thenReturn("REDBUS20251105ABC");
        when(bookingRepository.findByBookingReference("REDBUS20251105ABC")).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setBookingId(1L);
            booking.setCreatedAtEpoch(Instant.now().getEpochSecond());
            booking.setTripDetails(tripDetailsJson);
            return booking;
        });
        when(bookingPassengerRepository.findByBookingId(1L)).thenReturn(Collections.emptyList());
        when(objectMapper.writeValueAsString(any())).thenReturn(tripDetailsJson);
        when(objectMapper.readValue(eq(tripDetailsJson), eq(java.util.Map.class))).thenReturn(createTripDetailsMap());
        
        BookingResponse response = bookingService.createBooking(createRequest);

        assertNotNull(response);
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());
        assertEquals(BookingConstants.DEFAULT_TOTAL_AMOUNT, bookingCaptor.getValue().getTotalAmount());
    }

    @Test
    void testCreateBooking_NoPassengers() throws Exception {
        createRequest.setPassengers(null);
        String tripDetailsJson = "{\"trip_id\":\"200\",\"boarding_city_name\":\"Delhi\",\"dropping_city_name\":\"Mumbai\",\"booked_from_stop_sequence\":1,\"booked_to_stop_sequence\":5,\"boarding_datetime\":\"2025-11-05T10:00:00\",\"dropping_datetime\":\"2025-11-05T22:00:00\"}";
        
        when(bookingReferenceGenerator.generateBookingReference()).thenReturn("REDBUS20251105ABC");
        when(bookingRepository.findByBookingReference("REDBUS20251105ABC")).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setBookingId(1L);
            booking.setCreatedAtEpoch(Instant.now().getEpochSecond());
            booking.setTripDetails(tripDetailsJson);
            return booking;
        });
        when(bookingPassengerRepository.findByBookingId(1L)).thenReturn(Collections.emptyList());
        when(objectMapper.writeValueAsString(any())).thenReturn(tripDetailsJson);
        when(objectMapper.readValue(eq(tripDetailsJson), eq(java.util.Map.class))).thenReturn(createTripDetailsMap());
        
        BookingResponse response = bookingService.createBooking(createRequest);

        assertNotNull(response);
        verify(bookingPassengerRepository, never()).saveAll(any());
    }

    @Test
    void testCreateBooking_DuplicateReference() throws Exception {
        String tripDetailsJson = "{\"trip_id\":\"200\",\"boarding_city_name\":\"Delhi\",\"dropping_city_name\":\"Mumbai\",\"booked_from_stop_sequence\":1,\"booked_to_stop_sequence\":5,\"boarding_datetime\":\"2025-11-05T10:00:00\",\"dropping_datetime\":\"2025-11-05T22:00:00\"}";
        
        when(bookingReferenceGenerator.generateBookingReference())
                .thenReturn("REDBUS20251105ABC")
                .thenReturn("REDBUS20251105XYZ");
        when(bookingRepository.findByBookingReference("REDBUS20251105ABC"))
                .thenReturn(Optional.of(savedBooking));
        when(bookingRepository.findByBookingReference("REDBUS20251105XYZ"))
                .thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking booking = invocation.getArgument(0);
            booking.setBookingId(1L);
            booking.setCreatedAtEpoch(Instant.now().getEpochSecond());
            booking.setTripDetails(tripDetailsJson);
            return booking;
        });
        when(bookingPassengerRepository.findByBookingId(1L)).thenReturn(Collections.emptyList());
        when(objectMapper.writeValueAsString(any())).thenReturn(tripDetailsJson);
        when(objectMapper.readValue(eq(tripDetailsJson), eq(java.util.Map.class))).thenReturn(createTripDetailsMap());

        BookingResponse response = bookingService.createBooking(createRequest);

        assertNotNull(response);
        assertEquals("REDBUS20251105XYZ", response.getBookingReference());
        verify(bookingReferenceGenerator, times(2)).generateBookingReference();
    }

    @Test
    void testCreateBooking_TripDetailsSerializationFailure() throws Exception {
        when(bookingReferenceGenerator.generateBookingReference()).thenReturn("REDBUS20251105ABC");
        when(bookingRepository.findByBookingReference("REDBUS20251105ABC")).thenReturn(Optional.empty());
        when(objectMapper.writeValueAsString(any())).thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("Error") {});

        TripDetailsSerializationException exception = assertThrows(
                TripDetailsSerializationException.class,
                () -> bookingService.createBooking(createRequest)
        );

        assertNotNull(exception);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testGetBookingByReference_Success() throws Exception {
        String bookingReference = "REDBUS20251105ABC";
        when(bookingRepository.findByBookingReference(bookingReference))
                .thenReturn(Optional.of(savedBooking));
        when(bookingPassengerRepository.findByBookingId(1L)).thenReturn(passengers);
        when(objectMapper.readValue(anyString(), eq(java.util.Map.class)))
                .thenReturn(createTripDetailsMap());

        BookingResponse response = bookingService.getBookingByReference(bookingReference);

        assertNotNull(response);
        assertEquals(bookingReference, response.getBookingReference());
        assertEquals(200L, response.getTripId());
        assertNotNull(response.getBoardingDetails());
        assertEquals("Delhi", response.getBoardingDetails().getCityName());
        assertEquals(1, response.getBoardingDetails().getStopSequence());
        assertNotNull(response.getDroppingDetails());
        assertEquals("Mumbai", response.getDroppingDetails().getCityName());
        assertEquals(5, response.getDroppingDetails().getStopSequence());
        assertEquals(1, response.getPassengers().size());
    }

    @Test
    void testGetBookingByReference_NotFound() {
        String bookingReference = "INVALID123";
        when(bookingRepository.findByBookingReference(bookingReference))
                .thenReturn(Optional.empty());

        BookingNotFoundException exception = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.getBookingByReference(bookingReference)
        );

        assertTrue(exception.getMessage().contains(bookingReference));
    }

    @Test
    void testConfirmBooking_Success() throws Exception {
        String bookingReference = "REDBUS20251105ABC";
        ConfirmBookingRequest confirmRequest = ConfirmBookingRequest.builder()
                .bookingReferenceId(bookingReference)
                .paymentReferenceId("PAY123456")
                .build();

        Booking booking = Booking.builder()
                .bookingId(1L)
                .bookingReference(bookingReference)
                .userId(100L)
                .status(BookingStatus.INITIATED)
                .tripDetails("{\"trip_id\":\"200\"}")
                .totalAmount(1500.0)
                .createdAtEpoch(Instant.now().getEpochSecond())
                .build();

        when(bookingRepository.findByBookingReference(bookingReference))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingPassengerRepository.findByBookingId(1L))
                .thenReturn(Collections.emptyList());
        when(objectMapper.readValue(anyString(), eq(java.util.Map.class)))
                .thenReturn(java.util.Collections.singletonMap("trip_id", "200"));

        BookingResponse response = bookingService.confirmBooking(confirmRequest);

        assertNotNull(response);
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());
        Booking capturedBooking = bookingCaptor.getValue();
        assertEquals(BookingStatus.COMPLETED, capturedBooking.getStatus());
        assertEquals("PAY123456", capturedBooking.getPaymentReferenceId());
    }

    @Test
    void testConfirmBooking_NotFound() {
        String bookingReference = "INVALID123";
        ConfirmBookingRequest confirmRequest = ConfirmBookingRequest.builder()
                .bookingReferenceId(bookingReference)
                .paymentReferenceId("PAY123456")
                .build();

        when(bookingRepository.findByBookingReference(bookingReference))
                .thenReturn(Optional.empty());

        BookingNotFoundException exception = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.confirmBooking(confirmRequest)
        );

        assertTrue(exception.getMessage().contains(bookingReference));
    }

    @Test
    void testConfirmBooking_InvalidStatus() throws Exception {
        String bookingReference = "REDBUS20251105ABC";
        ConfirmBookingRequest confirmRequest = ConfirmBookingRequest.builder()
                .bookingReferenceId(bookingReference)
                .paymentReferenceId("PAY123456")
                .build();

        Booking booking = Booking.builder()
                .bookingId(1L)
                .bookingReference(bookingReference)
                .status(BookingStatus.COMPLETED) // Already completed
                .build();

        when(bookingRepository.findByBookingReference(bookingReference))
                .thenReturn(Optional.of(booking));

        InvalidBookingStatusException exception = assertThrows(
                InvalidBookingStatusException.class,
                () -> bookingService.confirmBooking(confirmRequest)
        );

        assertTrue(exception.getMessage().contains("INITIATED"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCancelBooking_Success() throws Exception {
        String bookingReference = "REDBUS20251105ABC";
        Booking booking = Booking.builder()
                .bookingId(1L)
                .bookingReference(bookingReference)
                .userId(100L)
                .status(BookingStatus.COMPLETED)
                .tripDetails("{\"trip_id\":\"200\"}")
                .totalAmount(1500.0)
                .createdAtEpoch(Instant.now().getEpochSecond())
                .build();

        when(bookingRepository.findByBookingReference(bookingReference))
                .thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingPassengerRepository.findByBookingId(1L))
                .thenReturn(Collections.emptyList());
        when(objectMapper.readValue(anyString(), eq(java.util.Map.class)))
                .thenReturn(java.util.Collections.singletonMap("trip_id", "200"));

        BookingResponse response = bookingService.cancelBooking(bookingReference);

        assertNotNull(response);
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());
        Booking capturedBooking = bookingCaptor.getValue();
        assertEquals(BookingStatus.CANCELLED, capturedBooking.getStatus());
    }

    @Test
    void testCancelBooking_NotFound() {
        String bookingReference = "INVALID123";
        when(bookingRepository.findByBookingReference(bookingReference))
                .thenReturn(Optional.empty());

        BookingNotFoundException exception = assertThrows(
                BookingNotFoundException.class,
                () -> bookingService.cancelBooking(bookingReference)
        );

        assertTrue(exception.getMessage().contains(bookingReference));
    }

    @Test
    void testCancelBooking_InvalidStatus() {
        String bookingReference = "REDBUS20251105ABC";
        Booking booking = Booking.builder()
                .bookingId(1L)
                .bookingReference(bookingReference)
                .status(BookingStatus.INITIATED) // Not completed
                .build();

        when(bookingRepository.findByBookingReference(bookingReference))
                .thenReturn(Optional.of(booking));

        InvalidBookingStatusException exception = assertThrows(
                InvalidBookingStatusException.class,
                () -> bookingService.cancelBooking(bookingReference)
        );

        assertTrue(exception.getMessage().contains("COMPLETED"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testToBookingResponse_WithCompleteTripDetails() throws Exception {
        when(objectMapper.readValue(anyString(), eq(java.util.Map.class)))
                .thenReturn(createTripDetailsMap());
        when(bookingRepository.findByBookingReference("REDBUS20251105ABC"))
                .thenReturn(Optional.of(savedBooking));

        BookingResponse response = bookingService.getBookingByReference("REDBUS20251105ABC");

        assertNotNull(response);
        assertEquals(200L, response.getTripId());
        assertNotNull(response.getBoardingDetails());
        assertEquals("Delhi", response.getBoardingDetails().getCityName());
        assertEquals(1, response.getBoardingDetails().getStopSequence());
        assertNotNull(response.getDroppingDetails());
        assertEquals("Mumbai", response.getDroppingDetails().getCityName());
        assertEquals(5, response.getDroppingDetails().getStopSequence());
    }

    private java.util.Map<String, Object> createTripDetailsMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("trip_id", "200");
        map.put("boarding_city_name", "Delhi");
        map.put("dropping_city_name", "Mumbai");
        map.put("booked_from_stop_sequence", 1);
        map.put("booked_to_stop_sequence", 5);
        map.put("boarding_datetime", "2025-11-05T10:00:00");
        map.put("dropping_datetime", "2025-11-05T22:00:00");
        return map;
    }
}

