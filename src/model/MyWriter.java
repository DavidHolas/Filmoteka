package model;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyWriter {

    public MyWriter() {
    }

    public void writeMovieUsingSerializable(List<Movie> movies) {

        try (FileOutputStream fos = new FileOutputStream(new File("movies.txt"));
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(movies);

            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> readMovieUsingSerializable() {

        List<Movie> movies = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File("movies.txt"));
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            movies = (ArrayList<Movie>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

    public void writeMoviesToXML(List<Movie> movies) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("filmy");

            for (Movie m : movies) {

                Element movie = doc.createElement("film");
                Attr attrName = doc.createAttribute("Název");
                attrName.setValue(m.getName());
                movie.setAttributeNode(attrName);

                Element genre = doc.createElement("zanr");
                genre.appendChild(doc.createTextNode(m.getGenre()));
                movie.appendChild(genre);

                Element year = doc.createElement("rok");
                String yearText = String.valueOf(m.getYear());
                year.appendChild(doc.createTextNode(yearText));
                movie.appendChild(year);

                root.appendChild(movie);
            }

            doc.appendChild(root);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("filmy_XML.xml"));

            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeActorsToXML(List<Actor> actors) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("herci");

            for (Actor a : actors) {

                Element actor = doc.createElement("herec");
                Attr attrName = doc.createAttribute("jmeno");
                attrName.setValue(a.getName());
                actor.setAttributeNode(attrName);
                Element year = doc.createElement("rok_narozeni");
                String yearText = String.valueOf(a.getYear());
                year.appendChild(doc.createTextNode(yearText));
                actor.appendChild(year);

                root.appendChild(actor);
            }

            doc.appendChild(root);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("herci_XML.xml"));

            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void writeMoviesToCSV(List<Movie> movies) {

        try {

            FileWriter csvWriter = new FileWriter("filmy_CSV.csv");

            csvWriter.append("Název");
            csvWriter.append(",");
            csvWriter.append("Žánr");
            csvWriter.append(",");
            csvWriter.append("Rok");
            csvWriter.append("\n");

            for (Movie m : movies) {

                csvWriter.append(m.getName());
                csvWriter.append(",");
                csvWriter.append(m.getGenre());
                csvWriter.append(",");
                csvWriter.append(String.valueOf(m.getYear()));
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public List<Movie> readMoviesFromXML() {

        List<Movie> list = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File("filmy_XML.xml"));
            Node root = doc.getDocumentElement();
            root.normalize();
            NodeList movies = root.getChildNodes();

            for (int i = 0; i < movies.getLength(); i++) {

                Node node = movies.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("film")) {

                    Element movie = (Element) node;
                    String name = movie.getAttribute("nazev");
                    String genre = movie.getElementsByTagName("zanr").item(0).getTextContent();
                    String year = movie.getElementsByTagName("rok").item(0).getTextContent();
                    Movie m = new Movie(name, genre, Integer.parseInt(year));
                    list.add(m);
                }
            }

        } catch (Exception e) {
            System.err.println("Chyba při čtení souboru: " + e.getMessage());
        }

        return list;
    }
}
