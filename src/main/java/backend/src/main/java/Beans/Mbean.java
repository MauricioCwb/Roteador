package Beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.context.annotation.ComponentScan;

import DTO.Point;
import DTO.Route;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan
public class Mbean {

	private static List<Route> routes = new ArrayList<Route>();
	private static List<Point> points = new ArrayList<Point>();
	static double[][] dist = null;
	static int[][] next = null;
	private static String pathNomeArquivo = "";
	private static final String VIRGULA = ",";

	public void inicializar(String pathArquivo) {

		carregarDados(pathArquivo);

		iniciarThread();
	}
	
	private void iniciarThread() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000 * 3);
				} catch (InterruptedException e) {}
				while(true) {	
					Scanner scanner = new Scanner(System.in);
					String rota;

					System.out.print("please enter the route: ");
					rota = scanner.next().toUpperCase();
					System.out.println(buscarResultado(rota));
				}
			}
		}.start(); 		
	}
	
	
	public void carregarDados(String pathNomeArquivo) {
		this.pathNomeArquivo = pathNomeArquivo;
		carregarDados();
	}
	
	private void carregarDados() {
		
		carregarRoutes();
		carregarPoints();
		
		int vertices = routes.size();
		int[][] matriz = new int[vertices][vertices];
		
		matriz = carregarMatriz(matriz);
		processarFloydWarshall(matriz, vertices);
	}
	
	private void carregarRoutes() {
		routes = new ArrayList<Route>();
        String linha = null;
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(pathNomeArquivo)));
        	while ((linha = reader.readLine()) != null) {
        		if(!"".equals(linha)) {
        			String[] campos = linha.split(VIRGULA);
        			routes.add(new Route(campos[0], campos[1], Integer.parseInt(campos[2])));
        		}
        	}
        	reader.close();
        } catch (IOException e) {
        	e.printStackTrace();
		}
	}

	private void carregarPoints() {
		List<String> allPoints = new ArrayList<String>();
		points = new ArrayList<Point>();
		
		for(Route route : routes) {
			incluirPoint(route.getFrom(), allPoints);
			incluirPoint(route.getTo(), allPoints);
		}
	}
	
	private void incluirPoint(String nome, List<String> allPoints) {
		if(allPoints.indexOf(nome) == -1) {
			allPoints.add(nome);
			points.add(new Point(nome, points.size()));
		}
	}
	
	public void inserirRota(String from, String to, int dist) {
		routes.add(new Route(from, to, dist));
		gravarArquivo();
		carregarDados();
	}

	public void gravarArquivo() {
		try (FileWriter writer = new FileWriter(pathNomeArquivo);
				BufferedWriter bw = new BufferedWriter(writer)) {
			for(Route route : routes) {
				bw.write(route.getFrom() + "," + route.getTo() + "," + route.getDist());
				bw.newLine();
			}
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
	
	private int[][] carregarMatriz(int[][] matriz) {
		int i = 0;
		for(Route route : routes) {
			matriz[i][0] = encontrarIndice(route.getFrom());
			matriz[i][1] = encontrarIndice(route.getTo());
			matriz[i][2] = route.getDist();
			i++;
		}
		return matriz;
	}
		
	private int encontrarIndice(String lugar) {
		int codigo = 1;
		for(Point point : points) {
			if(lugar.equals(point.getName())) {
				return codigo;
			}
			codigo ++;
		}
		return -1;
	}

	private static String encontrarLugar(int codigo) {
		int procura = codigo - 1;
		for(Point point : points) {
			if(procura == point.getCode()) {
				return point.getName();
			}
		}
		return "";
	}

	private static void processarFloydWarshall(int[][] matriz, int vertices) {
		 
        dist = new double[vertices][vertices];
        for (double[] row : dist) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }
 
        for (int[] w : matriz) {
            dist[w[0] - 1][w[1] - 1] = w[2];
        }
 
        next = new int[vertices][vertices];
        for (int i = 0; i < next.length; i++) {
            for (int j = 0; j < next.length; j++) {
                if (i != j) {
                    next[i][j] = j + 1;
                }
            }
        }
 
        for (int k = 0; k < vertices; k++) {
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }
	
    public static String buscarResultado(String trajetoParm) {
    	String[] trajeto = trajetoParm.split("-");
    	
    	for (int i = 0; i < next.length; i++) {
    		for (int j = 0; j < next.length; j++) {
    			if (i != j) {
    				int u = i + 1;
    				int v = j + 1;
    				if(trajeto[0].equals(encontrarLugar(u)) && trajeto[1].equals(encontrarLugar(v)) ) {
    					if(dist[i][j] == Double.POSITIVE_INFINITY) {
    						return "404";
    					} else {
    						String path = encontrarLugar(u);
    						do {
    							u = next[u - 1][v - 1];
    							path += " -> " + encontrarLugar(u);
    						} while (u != v);
    						return "Best route: " + path + " > " + dist[i][j];
    					}
    				}
    			}
    		}
        }
		return "404";
    }
	

}