package main.java.no.hiof.studytracker.model;

public class Article extends ArticleNote {

    public Article(int id, int userId, String title, String content, String createdAt, String updatedAt) {
        super(id, userId, title, content, createdAt, updatedAt);
    }

    public Article(int id, int userId, String title, String content, String createdAt) {
        super(id, userId, title, content, createdAt);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + getId() +
                ", userId=" + getUserId() +
                ", title='" + getTitle() + '\'' +
                ", content='" + getContent() + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}
