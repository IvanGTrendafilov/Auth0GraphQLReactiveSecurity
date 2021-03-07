package com.auth0.example;

import java.util.List;
import java.util.Objects;

public class Authorization {
    private List<String> groups;
    private List<String> permissions;
    private List<String> roles;

    public Authorization() {

    }

    public Authorization(final List<String> groups, final List<String> permissions, final List<String> roles) {
        this.groups = groups;
        this.permissions = permissions;
        this.roles = roles;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Authorization that = (Authorization) o;
        return Objects.equals(groups, that.groups) && Objects.equals(permissions, that.permissions) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, permissions, roles);
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(final List<String> groups) {
        this.groups = groups;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(final List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(final List<String> roles) {
        this.roles = roles;
    }
}
