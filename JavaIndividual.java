import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

// Base class for all types of videos
class Video {
    private String title;
    private String url;

    public Video(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Title: " + title + "\nURL: " + url + "\n";
    }
}

// Subclass for premium videos
class PremiumVideo extends Video {
    private double price;

    public PremiumVideo(String title, String url, double price) {
        super(title, url);
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

class YouTubeManager {
    private List<Video> videoList = new ArrayList<>();
    private Hashtable<String, List<Video>> videoCategories = new Hashtable<>();

    public void addVideo(Video video) {
        videoList.add(video);
    }

    public void removeVideo(int index) {
        if (index >= 0 && index < videoList.size()) {
            videoList.remove(index);
            System.out.println("Video removed successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    public void listVideos() {
        if(videoList.size()<1){
            System.out.println("No Videos.");
        }else{
        for (int i = 0; i < videoList.size(); i++) {
            System.out.println("Video " + (i + 1) + ":\n" + videoList.get(i));
        }}
    }

    public void categorizeVideos() {
        videoCategories.clear(); // Clear existing categories
        for (Video video : videoList) {
            String title = video.getTitle();
            if (!videoCategories.containsKey(title)) {
                videoCategories.put(title, new ArrayList<>());
            }
            videoCategories.get(title).add(video);
        }
    }

    public void listCategories() {
        System.out.println("Video Categories:");
        for (String category : videoCategories.keySet()) {
            System.out.println(category);
        }
    }

    public void listVideosInCategory(String category) {
        List<Video> videos = videoCategories.get(category);
        if (videos != null) {
            System.out.println("Videos in Category: " + category);
            for (Video video : videos) {
                System.out.println(video);
            }
        } else {
            System.out.println("Category not found.");
        }
    }

    public void saveVideosToFile(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(videoList);
            System.out.println("Videos saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving videos to file: " + e.getMessage());
        }
    }

    public void loadVideosFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            List<Video> loadedVideos = (List<Video>) inputStream.readObject();
            videoList.addAll(loadedVideos);
            System.out.println("Videos loaded from " + fileName);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading videos from file: " + e.getMessage());
        }
    }
}

 class Main1 {
    public static void main(String[] args) {
        YouTubeManager manager = new YouTubeManager();

        // Load videos from a file (if available)
        manager.loadVideosFromFile("videos.txt");

        // Menu for user interaction
        while (true) {
            System.out.println("Online Video Platform Management System");
            System.out.println("1. Add Video");
            System.out.println("2. Remove Video");
            System.out.println("3. List Videos");
            System.out.println("4. Categorize Videos");
            System.out.println("5. List Categories");
            System.out.println("6. List Videos in Category");
            System.out.println("7. Save Videos to File");
            System.out.println("8. Load Videos from File");
            System.out.println("9. Exit");

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                int choice = Integer.parseInt(reader.readLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter video title: ");
                        String title = reader.readLine();
                        System.out.print("Enter video URL: ");
                        String url = reader.readLine();
                        manager.addVideo(new Video(title, url));
                        System.out.println("Video added successfully.");
                        System.out.println();
                        break;
                    case 2:
                        System.out.print("Enter the index of the video to remove: ");
                        int index = Integer.parseInt(reader.readLine());
                        manager.removeVideo(index - 1);
                        System.out.println();
                        break;
                    case 3:
                        manager.listVideos();
                        break;
                    case 4:
                        manager.categorizeVideos();
                        System.out.println("Videos categorized.");
                        break;
                    case 5:
                        manager.listCategories();
                        break;
                    case 6:
                        System.out.print("Enter category name: ");
                        String category = reader.readLine();
                        manager.listVideosInCategory(category);
                        break;
                    case 7:
                        manager.saveVideosToFile("videos.txt");
                        break;
                    case 8:
                        manager.loadVideosFromFile("videos.txt");
                        break;
                    case 9:
                        System.out.println("Exiting.");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}