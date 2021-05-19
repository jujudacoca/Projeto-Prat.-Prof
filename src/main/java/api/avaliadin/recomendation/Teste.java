package api.avaliadin.recomendation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import api.avaliadin.model.Avaliacao;
import api.avaliadin.model.User;
import api.avaliadin.model.Amizade;
import api.avaliadin.repository.AmizadeRepository;
import api.avaliadin.repository.AvaliacaoRepository;
import api.avaliadin.repository.UserRepository;

// colocar tudo na class controler user pq ai pode acessar os repository

public class Teste {
	@Autowired
	private AvaliacaoRepository avaliacaoRepository;
	@NonNull
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AmizadeRepository amizadeRepository;
	
	public List<Ulikes> gerarLista(boolean amigo, int id) {
		List<Ulikes> l = new ArrayList<Ulikes>(); 
		List<Integer> lista = new ArrayList<Integer>();
		if(amigo) {
			Iterable<User> users = userRepository.findAll();
			Iterable<Amizade> amgs = amizadeRepository.findAll();
			
			boolean temamizade = false;
			for(User u: users) {
				temamizade = false;
				if(u.getId()==id){
					continue;
				}
				for(Amizade a: amgs) {
					if((u.getId()==a.getIdUser1())&&(id==a.getIdUser2())||(u.getId()==a.getIdUser2())&&(id==a.getIdUser1())) {
						temamizade = true;
						break;
					}
				}
				if(!temamizade){
					lista.add(u.getId());
				}
			}
		}else {
			System.out.println("");
		}
			System.out.println(lista);
		return l;
	}
	//algoritmo simples por similaridade
	public static double sim_pearson(List<Ulikes> l,int a1, int a2) {
		Map<Integer,Float> l1 = new HashMap<Integer,Float>();
		Map<Integer,Float> l2 = new HashMap<Integer,Float>();
		for(Ulikes u : l) {
			if(a1==u.getIduser()) {
				l1 = u.getLista();
			}else if(a2==u.getIduser()) {
				l2= u.getLista();
			}
			if(!l1.isEmpty() &&  !l2.isEmpty()){
				break;
			}
		}
		if(l1.isEmpty() ||  l2==null){
			return -1;
		}
		Map<Integer,Integer> si = new HashMap<Integer,Integer>();
		for(int i1 : l1.keySet() ) {
			if(l2.containsKey(i1)) {
				si.put(i1, 1);
			}
		}
		if (si.isEmpty()) {
			return 0;
		}
		int n = si.size();
		
		//soma
		float sum1 = 0;
		float sum2 = 0;
		//soma das quadrados
		float sum1sq = 0;
		float sum2sq = 0;
		//soma dos produtos
		float psum = 0;
		for(int i: si.keySet()){
			if(l1.containsKey(i) || l2.containsKey(i)) {
				sum1 += l1.get(i);
				sum2 += l2.get(i);
				sum1sq += Math.pow(l1.get(i), 2);
				sum2sq += Math.pow(l2.get(i),2);
				psum += l1.get(i)*l2.get(i);
			}
		}
		double num = psum - ((sum1*sum2)/2);
		double den = Math.sqrt((((sum1sq-Math.pow(sum1, 2))/n)*((sum2sq-Math.pow(sum2, 2))/n)));
		if(den==0) {
			return 0;
		}
		
		
		return num/den;
		
	}
	public static List<Integer> topMatches(List<Ulikes> l,int a1) {
		Map<Integer,Double> list = new HashMap<Integer,Double>();
		double sim = 0;
		for(Ulikes other: l) {
			if(other.getIduser()==a1) {
				continue;
			}
			sim = sim_pearson(l,a1,other.getIduser());
			System.out.print(other.getIduser());
			System.out.print("   ");
			System.out.println(sim);
			if(sim<=0) {
				sim = sim + 1;
			}
			list.put(other.getIduser(), sim);
		}
		List<Entry<Integer, Double>> rank = new ArrayList<>(list.entrySet());
		System.out.println(rank);
		rank.sort(Entry.comparingByValue());
		System.out.println(rank);
		int val = rank.size();
		if(rank.size()>=3) {
			val = 3;
		}
		List<Entry<Integer, Double>> rank2 = rank.subList(0, val);
		List<Integer> rec = new ArrayList<Integer>();
		for(int i=0;i<rank2.size();i++) {
			rec.add(rank2.get(i).getKey());
		}
		return rec;
	}
	
	public static List<Integer> getRecomendation(List<Ulikes> l,int a1) {
		Map<Integer,Float> l1 = new HashMap<Integer,Float>();
		Map<Integer,Float> l2 = new HashMap<Integer,Float>();
		Map<Integer,Double> totals = new HashMap<Integer,Double>();
		Map<Integer,Double> simsums = new HashMap<Integer,Double>();
		Map<Integer,Double> rank = new HashMap<Integer,Double>();
		double v = 0;
		for(Ulikes u : l) {
			if(a1==u.getIduser()) {
				l1 = u.getLista();
				break;
			}
		}
		for(Ulikes other: l) {
			if(other.getIduser()==a1) {
				continue;
			}
			double sim = sim_pearson(l,a1,other.getIduser());
			if(sim<=0) {
				continue;
			}
			l2 = other.getLista();
			for(int item: l2.keySet()) {
				if(!l1.containsKey(item)) {
					if(!totals.containsKey(item)) {
						totals.put(item,  0.00);
					}
					v = l2.get(item)*sim;
					totals.replace(item, v);
					if(!simsums.containsKey(item)) {
						simsums.put(item, 0.00);
					}
					v = simsums.get(item) + sim;
					simsums.put(item, v);
					v=(totals.get(item)/simsums.get(item));
				}
			}
		}
		for(int item: totals.keySet()) {
			rank.put(item,totals.get(item)/simsums.get(item));
			
		}
		List<Entry<Integer, Double>> list = new ArrayList<>(rank.entrySet());
		list.sort(Entry.comparingByValue());
		//reverse dps
		//testar com mais membros
		System.out.println(list);
		Collections.reverse(list);
		int val = list.size();
		if(list.size()>=3) {
			val = 3;
		}
		List<Entry<Integer, Double>> rank2 = list.subList(0, val);
		List<Integer> rec = new ArrayList<Integer>();
		for(int i=0;i<rank2.size();i++) {
			rec.add(rank2.get(i).getKey());
		}
		return rec;
	}
	
	public static void main(String[] args) {
		Teste a = new Teste();
		//a.gerarLista(true, 1);
		//double a = sim_pearson(gerarLista(true,1),1,4);
		//System.out.println(a);
		//List<Integer> b = getRecomendation(gerarLista(false,1),1);
		// TODO Auto-generated method stub
		//System.out.println(b);
		//List<Integer> c = topMatches(gerarLista(),1);
		
	}

	
}
