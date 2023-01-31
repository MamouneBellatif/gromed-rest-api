package fr.miage.gromed.service.auth;

import fr.miage.gromed.model.Utilisateur;

public class UserContextHolder {
    private static final ThreadLocal<Utilisateur> UTILISATEUR = new ThreadLocal<Utilisateur>(){
        @Override
        protected Utilisateur initialValue() {
            return null;
        }
    };

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
