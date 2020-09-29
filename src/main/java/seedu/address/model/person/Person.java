package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.project.Name;
import seedu.address.model.project.Participation;
import seedu.address.model.project.Project;
import seedu.address.model.tag.Tag;

/**
 * Represents a Teammate in the team.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private PersonName name;
    private Phone phone;
    private Email email;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private HashMap<Name, Participation> listOfParticipations = new HashMap<>();

    /**
     * Every field must be present and not null.
     */
    public Person(PersonName name, Phone phone, Email email, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
    }

    public PersonName getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public void updateName(String newNameStr) {
        name = new PersonName(newNameStr);
    }

    public void updatePhone(String newPhonestr) {
        phone = new Phone(newPhonestr);
    }

    public void updateEmail(String newEmailStr) {
        email = new Email(newEmailStr);
    }

    public void addProject(Project p) {
        listOfParticipations.put(p.getName(), new Participation(this, p));
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both teammates of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two projects.
     */
    public boolean isSameTeammate(Person otherTeammate) {
        if (otherTeammate == this) {
            return true;
        }

        return otherTeammate != null
                && otherTeammate.getName().equals(getName())
                && (otherTeammate.getPhone().equals(getPhone())
                || otherTeammate.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both projects have the same identity and data fields.
     * This defines a stronger notion of equality between two projects.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherProject = (Person) other;
        return otherProject.getName().equals(getName())
                && otherProject.getPhone().equals(getPhone())
                && otherProject.getEmail().equals(getEmail())
                && otherProject.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getPhone())
                .append(" Email: ")
                .append(getEmail())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
