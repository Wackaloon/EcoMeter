package com.alexanderageychenko.ecometer.Model.Depository.preloader;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by alexanderageychenko on 12/19/16.
 */

public enum PreloaderType {
    LOADING_METERS,
    GET_ADS_CONTENT;

    public static class Builder {

        HashMap<PreloaderType, HashSet<Long>> map = new HashMap<>();

        public Builder(PreloaderType type, Long id) {
            map = new HashMap<>();
            HashSet<Long> ids = new HashSet<>();
            ids.add(id);
            map.put(type, ids);
        }

        public Builder(PreloaderType type) {
            map = new HashMap<>();
            HashSet<Long> ids = new HashSet<>();
            map.put(type, ids);
        }
        public static PreloaderType.Builder getBuilder() {
            return new PreloaderType.Builder();
        }

        public Builder() {
            map = new HashMap<>();
        }

        public Builder add(PreloaderType type, Long id) {
            HashSet<Long> ids;
            if (map.containsKey(type)) {
                ids = map.get(type);
            } else {
                ids = new HashSet<>();
            }
            if (!ids.contains(id))
                ids.add(id);
            map.put(type, ids);
            return this;
        }

        public Builder add(PreloaderType type) {
            HashSet<Long> ids;
            if (map.containsKey(type)) {
                ids = map.get(type);
            } else {
                ids = new HashSet<>();
            }
            if (!ids.contains(-1L))
                ids.add(-1L);
            map.put(type, ids);
            return this;
        }


        public HashMap<PreloaderType, HashSet<Long>> build() {
            return map;
        }

    }
}
