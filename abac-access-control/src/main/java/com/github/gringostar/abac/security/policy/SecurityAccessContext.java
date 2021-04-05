package com.github.gringostar.abac.security.policy;

public class SecurityAccessContext {
    private Object subject;
    private Object resource;
    private Object action;
    private Object environment;

    public SecurityAccessContext(Object subject, Object resource, Object action, Object environment) {
        super();
        this.subject = subject;
        this.resource = resource;
        this.action = action;
        this.environment = environment;
    }

    public Object getSubject() {
        return subject;
    }

    public void setSubject(Object subject) {
        this.subject = subject;
    }

    public Object getResource() {
        return resource;
    }

    public void setResource(Object resource) {
        this.resource = resource;
    }

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        this.action = action;
    }

    public Object getEnvironment() {
        return environment;
    }

    public void setEnvironment(Object environment) {
        this.environment = environment;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + ((environment == null) ? 0 : environment.hashCode());
        result = prime * result + ((resource == null) ? 0 : resource.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SecurityAccessContext other = (SecurityAccessContext) obj;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.equals(other.action)) {
            return false;
        }

        if (objectNotEquals(environment, other.environment)) {
                return false;
        }
        if (objectNotEquals(resource, other.resource)) {
            return false;
        }
        return !objectNotEquals(subject, other.subject);
    }

    private boolean objectNotEquals(Object o, Object that) {
        if (o == null) {
            return that != null;
        } else {
            return !o.equals(that);
        }
    }
}
