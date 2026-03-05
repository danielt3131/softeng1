package edu.unf.smartplanner.persistence;

import edu.unf.smartplanner.model.PlannerData;

import java.io.*;

public class LocalStorage {

    public void save(PlannerData data, File file) throws IOException {
        if (data == null) throw new IllegalArgumentException("data required");
        if (file == null) throw new IllegalArgumentException("file required");

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(data);
        }
    }

    public PlannerData load(File file) throws IOException, ClassNotFoundException {
        if (file == null) throw new IllegalArgumentException("file required");
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = in.readObject();
            if (!(obj instanceof PlannerData)) throw new IOException("Invalid save file.");
            return (PlannerData) obj;
        }
    }
}
