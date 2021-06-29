package com.decompiler.util.output;

import com.decompiler.util.getopt.Options;
import com.decompiler.util.getopt.OptionsImpl;

public interface IllegalIdentifierDump {
    String getLegalIdentifierFor(String identifier);

    String getLegalShortName(String shortName);

    class Nop implements IllegalIdentifierDump {
        private static final IllegalIdentifierDump INSTANCE = new Nop();

        public static IllegalIdentifierDump getInstance() {
            return INSTANCE;
        }

        @Override
        public String getLegalIdentifierFor(String identifier) {
            return identifier;
        }

        @Override
        public String getLegalShortName(String shortName) {
            return shortName;
        }
    }

    class Factory {
        public static IllegalIdentifierDump get(Options options) {
            if (options.getOption(OptionsImpl.RENAME_ILLEGAL_IDENTS)) {
                return IllegalIdentifierReplacement.getInstance();
            } else {
                return Nop.getInstance();
            }
        }

        public static IllegalIdentifierDump getOrNull(Options options) {
            if (options.getOption(OptionsImpl.RENAME_ILLEGAL_IDENTS)) {
                return IllegalIdentifierReplacement.getInstance();
            } else {
                return null;
            }
        }
    }
}
