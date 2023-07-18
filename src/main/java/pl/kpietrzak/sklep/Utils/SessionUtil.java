package pl.kpietrzak.sklep.Utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class SessionUtil {

    public static boolean isUserLogged(HttpServletRequest request) {
        Object isUserLogged = request.getSession().getAttribute("isUserLogged");
        if (isUserLogged != null) {
            return (Boolean) isUserLogged;
        } else {
            return false;
        }
    }
}

