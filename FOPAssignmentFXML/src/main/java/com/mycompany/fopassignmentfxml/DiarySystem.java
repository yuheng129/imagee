package com.mycompany.fopassignmentfxml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DiarySystem {
    private ArrayList<Diary> diaryEntries = new ArrayList<>();
    private ArrayList<Diary> recycledEntries = new ArrayList<>();
    
    private String userFolder;
    
    
    public DiarySystem(String currentUser) {
        this.userFolder = currentUser;
        createUserFolder();
        readEntries();
    }
    
    
    public void readEntries() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.userFolder + "/" + this.userFolder + ".txt"))) {
            
            String line;
            while ((line = reader.readLine()) != null) {
                Diary entry = new Diary();
                String[] contents = line.split("\\|");
                entry.setDate(stringToLocalDate(contents[0]));
                entry.setTitle(contents[1]);
                entry.setMood(contents[2]);
                entry.setContent(contents[3]);
                entry.setStatus(contents[4]);
                entry.setLastModified(contents[5]);
                
                if (entry.getStatus().equals("Active")) {
                    diaryEntries.add(entry);
                }
                else {
                    recycledEntries.add(entry);
                }
                //System.out.println(entry.getContent().replace("`", "\n"));  //use this when displaying
                System.out.println(entry.toString() + " (read)"); //testing
            }
            System.out.println();

            reader.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addEntry(Diary entry) {
        diaryEntries.add(entry);
    }
    
    public ArrayList<Diary> getDiaryEntries() {
        return diaryEntries;
    }

    public void addRecycled(Diary entry) {
        recycledEntries.add(entry);
    }
    
    public ArrayList<Diary> getRecycledEntries() {
        return recycledEntries;
    }

    public String getUser() {
        return this.userFolder;
    }
    
    public void setUser(String username) {
        this.userFolder = username;
    }

    private void createUserFolder() {
        File mainFolder = new File(this.userFolder);
        File imagesFolder = new File(this.userFolder + "/Images");
        File recycleBinFolder = new File(this.userFolder + "/RecycleBin");
        File entryFile = new File(this.userFolder + "/" + this.userFolder + ".txt");
        
        try {
            // Create folder/file if it does not exist
            if (!mainFolder.exists()) {
                mainFolder.mkdir();
            }
            if (!imagesFolder.exists()) {
                imagesFolder.mkdir();
            }
            if (!recycleBinFolder.exists()) {
                recycleBinFolder.mkdir();
            }
            if (!entryFile.exists()) {
                entryFile.createNewFile();
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
    
    }
    
    public boolean saveEntries() { //always save after adding, editing and deleting
        
        // Save entries to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.userFolder + "/" + this.userFolder + ".txt"))) { // Use 'true' for appending
            for (Diary entry : this.diaryEntries) {
                writer.write(entry.toString());
                writer.newLine(); // Add a newline after each entry
                System.out.println(entry.toString() + "(Saved)"); //testing
            }
            System.out.println();
            
            for (Diary entry : this.recycledEntries) {
                writer.write(entry.toString());
                writer.newLine(); // Add a newline after each entry
                System.out.println(entry.toString() + "(Saved as Recycled)"); //testing
            }
            System.out.println();

            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("An error occurred while saving entries.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void recycleEntry(int entryIndex) {
        if (entryIndex < 0 || entryIndex >= diaryEntries.size()) {
            System.out.println("Invalid entry index.");
            return;
        }
        Diary entry = diaryEntries.get(entryIndex);
        entry.setStatus("Recycled");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        entry.setLastModified(LocalDate.now().format(formatter));


        if (!recycledEntries.contains(entry)) {
            recycledEntries.add(entry);
            System.out.println("####" + entry.toString() + "added to recycle bin");
        }
        diaryEntries.remove(entryIndex);

        if (diaryEntries.contains(entry))
            System.out.println("Entry not removed");
    }
    
    public static LocalDate stringToLocalDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dateStr, formatter);
    }

    public static String localDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public void cleanRecycled() {
        //remove recycled entries that are older than 30 days
    }
}
