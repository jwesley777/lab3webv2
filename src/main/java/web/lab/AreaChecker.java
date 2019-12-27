package web.lab;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@ManagedBean(name = "areaChecker")
@SessionScoped
public class AreaChecker implements Serializable {

    private static final long serialVersionUID = -3982895170546874882L;

    private Double x;
    private Double y;
    private Double r = 1d;

    private Double canvasX;
    private Double canvasY;

    private LinkedList<Point> points = new LinkedList<Point>();

    //private EntityManagerFactory entityManagerFactory;

    public AreaChecker() {
        //entityManagerFactory = Persistence.createEntityManagerFactory("default");
    }

    public void checkForm() {
        check(x, y);
    }

    public void checkCanvas() {
        check(canvasX, canvasY);
    }

    public void check(Double x, Double y) {
        EntityManager entityManager = HibernateUtils.getInstance().createEntityManager();
        entityManager.getTransaction().begin();
        Point point = new Point();
        point.setX(x);
        point.setY(y);
        point.setR(r);
        point.setHit(isHit(x, y));
        try {
            entityManager.persist(point);
            entityManager.getTransaction().commit();
            points.addFirst(point);
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }
        entityManager.close();
    }

    public boolean isHit(Point point) {
        return isHit(point.getX(), point.getY());
    }

    private boolean isHit(Double x, Double y) {
        double ax = r / 7.0;
        double ay = r / 6.0;

        boolean arc = (Math.pow(x,2) + Math.pow(y,2)) <= r*r && x >=0 && y >= 0;
        boolean square = x <= 0 && x >= -r && y >= 0 && y <= r;
        boolean line = y >= -x*2 - r && x <= 0 && y <= 0;
        if (arc || line || square) {
            return true;
        }
        return false;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public String getProcessedR() {
        if (r % 1 != 0) {
            return r + "";
        } else {
            return ((int)r.doubleValue()) + "";
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(LinkedList<Point> points) {
        this.points = points;
    }

    public Double getCanvasX() {
        return canvasX;
    }

    public void setCanvasX(Double canvasX) {
        this.canvasX = canvasX;
    }

    public Double getCanvasY() {
        return canvasY;
    }

    public void setCanvasY(Double canvasY) {
        this.canvasY = canvasY;
    }

}