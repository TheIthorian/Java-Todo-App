package com.todo.util;

import org.mindrot.jbcrypt.BCrypt;

public class Hasher implements IHasher {
    public String salt() {
        return BCrypt.gensalt();
    }

    public String hash(String str, String salt) {
        return BCrypt.hashpw(str, salt);
    }

    public boolean matches(String str, String hash) {
        return BCrypt.checkpw(str, hash);
    }
}
