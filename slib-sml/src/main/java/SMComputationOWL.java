
/* 
 *  Copyright or © or Copr. Ecole des Mines d'Alès (2012-2014) 
 *  
 *  This software is a computer program whose purpose is to provide 
 *  several functionalities for the processing of semantic data 
 *  sources such as ontologies or text corpora.
 *  
 *  This software is governed by the CeCILL  license under French law and
 *  abiding by the rules of distribution of free software.  You can  use, 
 *  modify and/ or redistribute the software under the terms of the CeCILL
 *  license as circulated by CEA, CNRS and INRIA at the following URL
 *  "http://www.cecill.info". 
 * 
 *  As a counterpart to the access to the source code and  rights to copy,
 *  modify and redistribute granted by the license, users are provided only
 *  with a limited warranty  and the software's author,  the holder of the
 *  economic rights,  and the successive licensors  have only  limited
 *  liability. 
 
 *  In this respect, the user's attention is drawn to the risks associated
 *  with loading,  using,  modifying and/or developing or reproducing the
 *  software by the user in light of its specific status of free software,
 *  that may mean  that it is complicated to manipulate,  and  that  also
 *  therefore means  that it is reserved for developers  and  experienced
 *  professionals having in-depth computer knowledge. Users are therefore
 *  encouraged to load and test the software's suitability as regards their
 *  requirements in conditions enabling the security of their systems and/or 
 *  data to be ensured and,  more generally, to use and operate it in the 
 *  same conditions as regards security. 
 * 
 *  The fact that you are presently reading this means that you have had
 *  knowledge of the CeCILL license and that you accept its terms.
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.OWL;
import org.openrdf.model.vocabulary.RDF;

import slib.graph.algo.accessor.GraphAccessor;
import slib.graph.algo.utils.GAction;
import slib.graph.algo.utils.GActionType;
import slib.graph.algo.validator.dag.ValidatorDAG;
import slib.graph.io.conf.GDataConf;
import slib.graph.io.conf.GraphConf;
import slib.graph.io.loader.GraphLoaderGeneric;
import slib.graph.io.util.GFormat;
import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;
import slib.graph.model.graph.utils.Direction;
import slib.graph.model.impl.graph.memory.GraphMemory;
import slib.graph.model.impl.repo.URIFactoryMemory;
import slib.graph.model.repo.URIFactory;
import slib.sml.sm.core.engine.SM_Engine;
import slib.sml.sm.core.metrics.ic.utils.IC_Conf_Corpus;
import slib.sml.sm.core.metrics.ic.utils.IC_Conf_Topo;
import slib.sml.sm.core.metrics.ic.utils.ICconf;
import slib.sml.sm.core.utils.SMConstants;
import slib.sml.sm.core.utils.SMParams;
import slib.sml.sm.core.utils.SMconf;
import slib.utils.ex.SLIB_Exception;
import slib.utils.impl.Timer;
 
/**
 *
 * @author Sébastien Harispe <sebastien.harispe@gmail.com>
 */
public class SMComputationOWL {
 
    public static void main(String[] args) throws SLIB_Exception { URIFactory factory = URIFactoryMemory.getSingleton();
    URI graph_uri = factory.getURI("http://go/");
	 
    // We define a prefix in order to build valid uris from ids such as GO:XXXXX, 
    // considering the configuration specified below the URI associated 
    // to GO:XXXXX will be http://go/XXXXX
    factory.loadNamespacePrefix("GO", graph_uri.toString());

    // We configure the graph
    GraphConf graphConf = new GraphConf(graph_uri);
//    graphConf.addGDataConf(new GDataConf(GFormat.RDF_XML, "/home/aaronayllon/Documents/go-07-03-2018.owl"));
    graphConf.addGDataConf(new GDataConf(GFormat.OBO, "/home/aaronayllon/Documents/go.obo"));
    graphConf.addGDataConf(new GDataConf(GFormat.GAF2, "/home/aaronayllon/Documents/goa_human-30-08-2017.gaf"));

    GAction rooting = new GAction(GActionType.REROOTING);
    rooting.addParameter("root_uri", OWL.THING.stringValue());

    graphConf.addGAction(rooting);
    G graph = GraphLoaderGeneric.load(graphConf);
    for(E e : graph.getE(factory.getURI("http://go/0030883"),Direction.BOTH)){
    	System.out.println(e);
    }
    
    // General information about the graph
    System.out.println(graph.toString());

    // We retrieve only the classes, i.e. GO terms
    // Note that the graph also contains the genes
    Set<URI> goTerms = GraphAccessor.getClasses(graph);
    System.out.println("GO terms : " + goTerms.size());
   
    // We configure the IC
    ICconf icConfRes = new IC_Conf_Corpus(SMConstants.FLAG_IC_ANNOT_RESNIK_1995);
    
    SM_Engine engine = new SM_Engine(graph);
  
    
    System.out.println(Collections.max(engine.getNbInstancesInferredPropFromCorpus(false).values()));
    	//    for (URI goTerm : goTerms) {

        System.out.println("http://purl.obolibrary.org/obo/GO_0030883" + "\t" + 46829*Math.exp(-engine.getIC(icConfRes, factory.getURI("http://go/0030883"))));
//    }
    }
    
    }