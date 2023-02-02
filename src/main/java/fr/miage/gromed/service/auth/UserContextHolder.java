package fr.miage.gromed.service.auth;

import fr.miage.gromed.model.Utilisateur;

public class UserContextHolder {
    private static final ThreadLocal<Utilisateur> UTILISATEUR = ThreadLocal.withInitial(() -> null);

    public static Utilisateur getUtilisateur() {
        return UTILISATEUR.get();
    }

    public static void setUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            throw new NullPointerException("utilisateur null");
        }
        UTILISATEUR.set(utilisateur);
    }

    public static void clear() {
        UTILISATEUR.remove();
    }

}
