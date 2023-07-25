package com.example.webfluxdemo;

import com.example.webfluxdemo.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchRuntimeException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MockitoTests {
    
    @Mock
    private User mockUser;
    @Mock
    private List<User> mockList;
    @Spy
    private List<User> spyList = new ArrayList<>();

    @Test
    void testVerify() {
        String user1 = "user1";
        String user2 = "user2";
        when(mockUser.getName()).thenReturn(user1, user2);

        assertThat(mockUser.getName()).isEqualTo(user1);
        assertThat(mockUser.getName()).isEqualTo(user2);

        verify(mockUser, times(2)).getName();
        verify(mockUser, atLeast(1)).getName();
        verify(mockUser, atMost(2)).getName();
        verify(mockUser, never()).getPass();
    }

    @Test
    void testInOrder() {
        String user = "user";
        String pass = "pass";
        doReturn(user).when(mockUser).getName();
        doReturn(pass).when(mockUser).getPass();
        InOrder inOrder = inOrder(mockUser);

        assertThat(mockUser.getPass()).isEqualTo(pass);
        assertThat(mockUser.getName()).isEqualTo(user);

        inOrder.verify(mockUser).getPass();
        inOrder.verify(mockUser).getName();
    }

    @Test
    void testSpy() {
        doReturn(true).when(mockList).add(mockUser);

        assertThat(mockList.add(mockUser)).isTrue();
        assertThat(mockList.size()).isZero();

        assertThat(spyList.add(mockUser)).isTrue();
        assertThat(spyList).hasSize(1);

        doReturn(10).when(spyList).size();
        assertThat(spyList).hasSize(10);
    }

    @Test
    void testThrow() {
        doThrow(RuntimeException.class).when(mockUser).setName(anyString());
        catchRuntimeException(() -> mockUser.setName(""));
    }
}
