package com.todo.util;

public interface IHasher {
    String salt();

    String hash(String str, String salt);

    boolean matches(String str, String hash);
}
