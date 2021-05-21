package api.avaliadin.recomendation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import api.avaliadin.repository.AmizadeRepository;
import api.avaliadin.repository.AvaliacaoRepository;
import api.avaliadin.repository.ItemRepository;
import api.avaliadin.repository.RecomendacaoRepository;
import api.avaliadin.repository.UserRepository;
import api.avaliadin.model.*;

@Component
public class ScheduledTasks {
	
	@Autowired
	private RecomendacaoRepository rp;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AmizadeRepository amizadeRepository;
	@Autowired
	private AvaliacaoRepository avaliacaoRepository;
	@Autowired
	private ItemRepository itemRepository;

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(fixedDelay = 60000*120, initialDelay = 60000*10)
	public void atualizarecomendacao() {
		log.info("The time is now {}", dateFormat.format(new Date()));
		Iterable<User> users = userRepository.findAll();
		Iterable<Avaliacao> la = avaliacaoRepository.findAll();
		Iterable<Amizade> amgs = amizadeRepository.findAll();
		Iterable<Item> items = itemRepository.findAll();
		rp.deleteAll();
		List<Ulikes> listaTotal = gerarListaTotal(); 
		for(User u: users) {
			List<Integer> listaAmigo = topMatches(gerarLista(u.getId(),la,users,amgs),u.getId());
			List<Integer> listaItem = getRecomendation(listaTotal,u.getId(),items);
			Recomendacao r = new Recomendacao();
			r.setIdUser(u.getId());
			r.setIduser1(listaAmigo.get(0));
			r.setIduser2(listaAmigo.get(1));
			r.setIduser3(listaAmigo.get(2));
			r.setIdItem1(listaItem.get(0));
			r.setIdItem2(listaItem.get(1));
			r.setIdItem3(listaItem.get(2));
			rp.save(r);
		}
	}
	

		
		
	
	
	public List<Ulikes> gerarLista( int id,Iterable<Avaliacao> la,Iterable<User> users,Iterable<Amizade> amgs) {
		List<Ulikes> l = new ArrayList<Ulikes>(); 
		
			List<Integer> lista = new ArrayList<Integer>();
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
			for(int i: lista) {
				Map<Integer,Float> map = new HashMap<Integer,Float>();
				for(Avaliacao a: la) {
					if(a.getIdUsuario()==i) {
						map.put(a.getIdItem(), a.getNota());
						}
				}
				Ulikes o = new Ulikes();
				o.setIduser(i);
				o.setLista(map);
				l.add(o);
			}
		
		return l;
	}
	public List<Ulikes> gerarListaTotal() {
		List<Ulikes> l = new ArrayList<Ulikes>(); 
		Iterable<Avaliacao> la = avaliacaoRepository.findAll();
		
		Iterable<User> users = userRepository.findAll();
		for(User u : users) {
			Map<Integer,Float> map = new HashMap<Integer,Float>();
			for(Avaliacao a: la) {
				if(a.getIdUsuario()==u.getId()) {
					map.put(a.getIdItem(), a.getNota());
				}
			}
			Ulikes o = new Ulikes();
			o.setIduser(u.getId());
			o.setLista(map);
			l.add(o);
		}
		return l;
	}
	public double sim_pearson(List<Ulikes> l,int a1, int a2) {
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
	public List<Integer> topMatches(List<Ulikes> l,int a1) {
		Map<Integer,Double> list = new HashMap<Integer,Double>();
		double sim = 0;	
		for(Ulikes other: l) {
			if(other.getIduser()==a1) {
				continue;
			}
			sim = sim_pearson(l,a1,other.getIduser());
			if(sim<=0) {
				sim = sim + 1;
			}
			list.put(other.getIduser(), sim);
		}
		
		List<Entry<Integer, Double>> rank = new ArrayList<>(list.entrySet());
		rank.sort(Entry.comparingByValue());
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
	public List<Integer> getRecomendation(List<Ulikes> l,int a1,Iterable<Item> items) {
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
						totals.put(item,  1.0);
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
		if(rec.isEmpty()) {
			Iterator<Item> it = items.iterator();
			List<Integer> rec2 = new ArrayList<Integer>();
			while(it.hasNext()) {
				rec2.add(it.next().getId());
				if(l1.containsKey(rec2.get(rec2.size()-1))) {
					rec2.remove(rec2.size()-1);
				}
			}
			
			val = rec2.size();
			if(rec2.size()>=3) {
				val = 3;
				}
			rec = rec2.subList(0, val);
			
		}
		return rec;
	}
}
