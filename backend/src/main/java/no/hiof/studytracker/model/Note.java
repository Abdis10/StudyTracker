package no.hiof.studytracker.model;

public class Note extends ArticleNote {
    public Note(int id, int userId, String title, String content, String createdAt, String updatedAt) {
        super(id, userId, title, content, createdAt, updatedAt);
    }

    public Note(int id, int userId, String title, String content, String createdAt) {
        super(id, userId, title, content, createdAt);
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + getId() +
                ", userId=" + getUserId() +
                ", title='" + getTitle() + '\'' +
                ", content='" + getContent() + '\'' +
                ", createdAt='" + getCreatedAt() + '\'' +
                ", updatedAt='" + getUpdatedAt() + '\'' +
                '}';
    }
}
